package com.utaite.player.view.main

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.utaite.player.BuildConfig
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import com.utaite.player.rest.Data
import com.utaite.player.rest.DataUtil
import com.utaite.player.rest.RestUtil
import com.utaite.player.rest.WATCH
import com.utaite.player.util.*
import com.utaite.player.util.SettingUtil.Companion.MARKET_URL
import com.utaite.player.view.list.ListFragment
import com.utaite.player.view.list.SORTED_NEWEST_UPLOAD
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function7
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_activity_await.*


private const val AYAPONZU = "Ayaponzu"
private const val HIINA = "Hiina"
private const val KUROKUMO = "Kurokumo"
private const val LAILAI = "LaiLai"
private const val NAMELESS = "Nameless"
private const val RIBONNU = "Ribonnu"
private const val WOTAMIN = "Wotamin"
private const val YUIKONNU = "Yuikonnu"


class MainActivity : BaseActivity() {

    override val layoutId: Int = R.layout.common_activity_await
    override val self = this@MainActivity

    private val menuSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    private val backSubject: BehaviorSubject<Long> = BehaviorSubject.createDefault(0L)
    private val backDisposable =
            backSubject
                    .observeOn(AndroidSchedulers.mainThread())
                    .buffer(2, 1)
                    .map { it[0] to it[1] }
                    .subscribe({ (first, second) ->
                        when {
                            SettingUtil.RECYCLER_SPAN_COUNT == 0 -> finish()
                            PreferenceUtil.getInstance(applicationContext).getBoolean(IS_INIT, true) -> Unit
                            second - first > SettingUtil.BACK_PRESS_TIME -> ToastUtil.getInstance(applicationContext).text(self, R.string.onBackPressed)
                            else -> finish()
                        }
                    })
                    .apply { disposables.add(this) }

    override fun onBackPressed() =
            backSubject.onNext(System.currentTimeMillis())

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.orientation) {
            android.content.res.Configuration.ORIENTATION_LANDSCAPE -> SettingUtil.RECYCLER_SPAN_COUNT = 3
            android.content.res.Configuration.ORIENTATION_PORTRAIT -> SettingUtil.RECYCLER_SPAN_COUNT = 2
        }
        loadViewPager(currentPosition = mainViewPager.currentItem)
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menuSubject
                .subscribe({ isVisible ->
                    menu.findItem(R.id.mainMenuSort).isVisible = isVisible
                    menu.findItem(R.id.mainMenuUpdate).isVisible = isVisible
                }, {
                    Log.e(ERROR, it.toString())
                    finish()
                })
                .apply { disposables.add(this) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainMenuSort -> {
                val sortIndex: Int = PreferenceUtil.getInstance(applicationContext).getInt(IS_SORTED, 0)
                val sortList: Array<String> = arrayOf(
                        getString(R.string.main_sort_latest_upload),
                        getString(R.string.main_sort_most_view),
                        getString(R.string.main_sort_title)
                )
                AlertDialog.Builder(self).run {
                    setTitle(getString(R.string.main_sort_alert_title))
                    setSingleChoiceItems(sortList, sortIndex, { dialog, which ->
                        PreferenceUtil.getInstance(applicationContext).setInt(IS_SORTED, which)
                        dialog.dismiss()
                        loadViewPager(currentPosition = mainViewPager.currentItem)
                    })
                    show()
                }
                return true
            }
            R.id.mainMenuUpdate -> {
                val currentCount = Realm.getDefaultInstance().where(Data::class.java).count().toInt()
                val totalCount = PreferenceUtil.getInstance(applicationContext).getInt(TOTAL_COUNT, 0)
                val latestCount = totalCount - currentCount

                when (latestCount == 0) {
                    false -> getDialog(self, getString(R.string.main_update_possible, latestCount)).apply {
                        setPositiveButton(getString(R.string.common_alert_positive), { dialog, _ ->
                            dialog.dismiss()

                            PreferenceUtil.getInstance(applicationContext).setBoolean(IS_INIT, true)
                            finish()
                            overridePendingTransition(0, 0)
                            startActivity(Intent(self, MainActivity::class.java))
                        })
                        setNegativeButton(getString(R.string.common_alert_negative), { dialog, _ -> dialog.dismiss() })
                        show()
                    }
                    true -> getDialog(self, R.string.main_update_impossible).apply {
                        setPositiveButton(getString(R.string.common_alert_positive), { dialog, _ -> dialog.dismiss() })
                        show()
                    }
                }
                return true
            }
        }
        return false
    }

    override fun init() {
        supportActionBar?.setTitle(self, getString(R.string.app_name))
        menuSubject.onNext(false)

        when (networkCheck(self)) {
            false -> ToastUtil.getInstance(applicationContext).text(self, R.string.common_network_error)
            true -> {
                awaitProgressBar.visibility = View.VISIBLE
                val currentVersion: Int = BuildConfig.VERSION_NAME.toVersion()

                RestUtil.getInformation()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ information ->
                            PreferenceUtil.getInstance(applicationContext).setInt(TOTAL_COUNT, information.totalCount)

                            when (currentVersion >= information.minVersion.toVersion()) {
                                false -> {
                                    awaitProgressBar.visibility = View.GONE
                                    getDialog(self, R.string.main_version_error).apply {
                                        setPositiveButton(getString(R.string.common_alert_positive), { dialog, _ ->
                                            dialog.dismiss()
                                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL)))
                                            finish()
                                        })
                                        setNegativeButton(getString(R.string.common_alert_negative), { dialog, _ -> dialog.dismiss() })
                                        show()
                                    }
                                }
                                true -> when (currentVersion >= information.warningVersion.toVersion()) {
                                    false -> getDialog(self, getString(R.string.main_version_warning, information.newVersion)).apply {
                                        setPositiveButton(getString(R.string.common_alert_positive), { dialog, _ ->
                                            dialog.dismiss()
                                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL)))
                                            finish()
                                        })
                                        setNegativeButton(getString(R.string.common_alert_negative), { dialog, _ ->
                                            dialog.dismiss()
                                            loadData()
                                        })
                                        setCancelable(false)
                                        show()
                                    }
                                    true -> loadData()
                                }
                            }
                        }, {
                            Log.e(NETWORK_ERROR, it.toString())
                            getDialog(self, R.string.main_version_error).apply {
                                setPositiveButton(getString(R.string.common_alert_positive), { dialog, _ ->
                                    dialog.dismiss()
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL)))
                                    finish()
                                })
                                setNegativeButton(getString(R.string.common_alert_negative), { dialog, _ -> dialog.dismiss() })
                                show()
                            }
                        })
                        .apply { disposables.add(this) }
            }
        }
    }

    private fun String.toVersion(): Int =
            replace(".", "").toInt()

    private fun loadData() {
        when (resources.configuration.orientation) {
            android.content.res.Configuration.ORIENTATION_LANDSCAPE -> SettingUtil.RECYCLER_SPAN_COUNT = 3
            android.content.res.Configuration.ORIENTATION_PORTRAIT -> SettingUtil.RECYCLER_SPAN_COUNT = 2
        }

        val isInit: Boolean = PreferenceUtil.getInstance(applicationContext).getBoolean(IS_INIT, true)
        when (isInit) {
            false -> loadTabLayout()
            true -> {
                Realm.getDefaultInstance().executeTransaction { it.delete(Data::class.java) }

                Observable.zip(RestUtil.getUtaiteData(AYAPONZU),
                        RestUtil.getUtaiteData(HIINA),
                        RestUtil.getUtaiteData(KUROKUMO),
                        RestUtil.getUtaiteData(LAILAI),
                        RestUtil.getUtaiteData(NAMELESS),
                        RestUtil.getUtaiteData(RIBONNU),
                        RestUtil.getUtaiteData(YUIKONNU),
                        Function7 { ayaponzu: List<Data>, hiina: List<Data>, kurokumo: List<Data>, lailai: List<Data>, nameless: List<Data>, ribonnu: List<Data>, yuikonnu: List<Data> ->
                            DataUtil.initAyaponzu(ayaponzu)
                            DataUtil.initHiina(hiina)
                            DataUtil.initKurokumo(kurokumo)
                            DataUtil.initLailai(lailai)
                            DataUtil.initNameless(nameless)
                            DataUtil.initRibonnu(ribonnu)
                            DataUtil.initYuikonnu(yuikonnu)

                            val list: MutableList<Data> = mutableListOf()
                            list.apply { addAll(ayaponzu, hiina, kurokumo, lailai, nameless, ribonnu, yuikonnu) }
                        })
                        .flatMapIterable { it }
                        .flatMap { RestUtil.getInfo(it.watch) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete {
                            PreferenceUtil.getInstance(applicationContext).setBoolean(IS_INIT, false)
                            PreferenceUtil.getInstance(applicationContext).setInt(IS_SORTED, SORTED_NEWEST_UPLOAD)
                            loadTabLayout()
                        }
                        .observeOn(Schedulers.io())
                        .subscribe({ info ->
                            Realm.getDefaultInstance().executeTransaction {
                                val url: String = info.thumb.watchUrl.run { substring(indexOf("sm") + 2) }
                                val data: Data? = it.where(Data::class.java).equalTo(WATCH, url).findFirst()
                                data?.count = info.thumb.viewCounter.toInt()
                            }
                        }, {
                            Log.e(NETWORK_ERROR, it.toString())
                            finish()
                        })
                        .apply { disposables.add(this) }
            }
        }
    }

    private fun loadTabLayout() {
        setContentView(getView(self, R.layout.activity_main))
        menuSubject.onNext(true)

        val dataSet: List<ListFragment> = getDataSet()
        mainTabLayout.run {
            setupWithViewPager(mainViewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {}
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabSelected(tab: TabLayout.Tab) {
                    supportActionBar?.setTitle(self, dataSet.getUtaite(selectedTabPosition))
                    mainViewPager.setCurrentItem(tab.position, false)
                }
            })
        }

        loadViewPager()
    }

    private fun loadViewPager(currentPosition: Int = 0) {
        val dataSet: List<ListFragment> = getDataSet()
        val random: Int = (Math.random() * dataSet.size).toInt()
        mainViewPager.run {
            adapter = MainAdapter(supportFragmentManager, self, dataSet)
            currentItem = when (currentPosition == 0) {
                false -> currentPosition
                true -> SettingUtil.TAB_MAX_VALUE / (2 * dataSet.size) * dataSet.size + random
            }
        }

        val tab = mainTabLayout.getChildAt(0) as LinearLayout
        (0 until mainTabLayout.tabCount)
                .filter { tab.getChildAt(it) is LinearLayout }
                .map { tab.getChildAt(it) as LinearLayout }
                .filter { it.getChildAt(1) is TextView }
                .map { it.getChildAt(1) as TextView }
                .forEach { it.setFont(self) }
    }

    private fun getDataSet(): List<ListFragment> =
            listOf(
                    newListInstance(R.string.utaite_nameless),
                    newListInstance(R.string.utaite_lailai),
                    newListInstance(R.string.utaite_ribonnu),
                    newListInstance(R.string.utaite_ayaponzu),
                    newListInstance(R.string.utaite_yuikonnu),
                    newListInstance(R.string.utaite_kurokumo),
                    newListInstance(R.string.utaite_hiina)
            )

    private fun MutableList<Data>.addAll(vararg dataList: List<Data>) =
            dataList.forEach { addAll(it) }

}
