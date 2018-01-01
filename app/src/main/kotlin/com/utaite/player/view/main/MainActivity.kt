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
import com.utaite.player.util.*
import com.utaite.player.view.list.ListFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_main
    override val self = this@MainActivity

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.orientation) {
            android.content.res.Configuration.ORIENTATION_LANDSCAPE -> SettingUtil.RECYCLER_SPAN_COUNT = 3
            android.content.res.Configuration.ORIENTATION_PORTRAIT -> SettingUtil.RECYCLER_SPAN_COUNT = 2
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                R.id.mainMenuSorted -> {
                    val sortedIndex: Int = PreferenceUtil.getInstance(applicationContext).getInt(SORTED, 0)
                    val sortedList: Array<String> = arrayOf(
                            getString(R.string.list_sorted_newest_upload),
                            getString(R.string.list_sorted_title),
                            getString(R.string.list_sorted_most_view)
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
                    true
                }
                else -> false
            }

    override fun init() {
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
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ loadTabLayout() }, { Log.e(TAG, it.toString()) })
                        .apply { disposables.add(this) }

                PreferenceUtil.getInstance(applicationContext).setBoolean(INIT, false)
            }
        }
    }

    private fun loadTabLayout() {
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

}
