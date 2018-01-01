package com.utaite.player.view.main

import android.content.res.Configuration
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import com.utaite.player.rest.Data
import com.utaite.player.rest.DataUtil
import com.utaite.player.rest.RestUtil
import com.utaite.player.rest.URL
import com.utaite.player.util.*
import com.utaite.player.view.list.ListFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


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
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menuSubject.subscribe({ menu.findItem(R.id.mainMenuSorted).isVisible = it }, { Log.e(ERROR, it.toString()) })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mainMenuSorted) {
            val sortedIndex: Int = PreferenceUtil.getInstance(applicationContext).getInt(SORTED, 0)
            val sortedList: Array<String> = arrayOf(
                    getString(R.string.list_sorted_newest_upload),
                    getString(R.string.list_sorted_most_view),
                    getString(R.string.list_sorted_title)
            )
            AlertDialog.Builder(self).run {
                setTitle(getString(R.string.list_sorted_alert_title))
                setSingleChoiceItems(sortedList, sortedIndex, { dialog, which ->
                    PreferenceUtil.getInstance(applicationContext).setInt(SORTED, which)
                    dialog.dismiss()
                    loadViewPager(currentPosition = mainViewPager.currentItem)
                })
                show()
            }
            return true
        }
        return false
    }

    override fun init() {
        supportActionBar?.setTitle(self, getString(R.string.app_name))
        menuSubject.onNext(false)

        when (resources.configuration.orientation) {
            android.content.res.Configuration.ORIENTATION_LANDSCAPE -> SettingUtil.RECYCLER_SPAN_COUNT = 3
            android.content.res.Configuration.ORIENTATION_PORTRAIT -> SettingUtil.RECYCLER_SPAN_COUNT = 2
        }

        val isInit: Boolean = PreferenceUtil.getInstance(applicationContext).getBoolean(INIT, true)
        when (isInit) {
            false -> loadTabLayout()
            true -> {
                Realm.getDefaultInstance().executeTransaction { it.delete(Data::class.java) }

                Observable.zip(RestUtil.getHiinaData(),
                        RestUtil.getKurokumoData(),
                        RestUtil.getNamelessData(),
                        RestUtil.getYuikonnuData(),
                        Function4 { hiina: List<Data>, kurokumo: List<Data>, nameless: List<Data>, yuikonnu: List<Data> ->
                            DataUtil.initHiina(hiina)
                            DataUtil.initKurokumo(kurokumo)
                            DataUtil.initNameless(nameless)
                            DataUtil.initYuikonnu(yuikonnu)

                            val list: MutableList<Data> = mutableListOf()
                            list.apply { addAll(hiina, kurokumo, nameless, yuikonnu) }
                        })
                        .flatMap {
                            Observable.fromIterable(it)
                                    .flatMap { RestUtil.getInfo(it.url) }
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { loadTabLayout() }
                        .observeOn(Schedulers.io())
                        .subscribe({ info ->
                            Realm.getDefaultInstance().executeTransaction {
                                val url: String = info.thumb.watchUrl.run { substring(indexOf("sm") + 2) }
                                val data: Data? = it.where(Data::class.java).equalTo(URL, url).findFirst()
                                data?.count = info.thumb.viewCounter.toInt()
                            }
                        }, { Log.e(NETWORK_ERROR, it.toString()) })
                        .apply { disposables.add(this) }

                PreferenceUtil.getInstance(applicationContext).setBoolean(INIT, false)
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
                    newListInstance(R.string.utaite_hiina),
                    newListInstance(R.string.utaite_kurokumo),
                    newListInstance(R.string.utaite_nameless),
                    newListInstance(R.string.utaite_yuikonnu)
            )

    private fun MutableList<Data>.addAll(vararg dataList: List<Data>) =
            dataList.forEach { addAll(it) }


}
