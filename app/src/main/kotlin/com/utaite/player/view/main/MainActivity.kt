package com.utaite.player.view.main

import android.content.Context
import android.content.res.Configuration
import android.support.design.widget.TabLayout
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import com.utaite.player.data.Data
import com.utaite.player.data.DataUtil
import com.utaite.player.rest.RestUtil
import com.utaite.player.util.*
import com.utaite.player.view.list.ListFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*


private const val INIT = "INIT"


class MainActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_main
    override val self: Context = this@MainActivity

    override fun onConfigurationChanged(newConfig: Configuration) {
        when (newConfig.orientation) {
            android.content.res.Configuration.ORIENTATION_LANDSCAPE -> SettingUtil.RECYCLER_SPAN_COUNT = 3
            android.content.res.Configuration.ORIENTATION_PORTRAIT -> SettingUtil.RECYCLER_SPAN_COUNT = 2
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun init() {
        when (resources.configuration.orientation) {
            android.content.res.Configuration.ORIENTATION_LANDSCAPE -> SettingUtil.RECYCLER_SPAN_COUNT = 3
            android.content.res.Configuration.ORIENTATION_PORTRAIT -> SettingUtil.RECYCLER_SPAN_COUNT = 2
        }

        val isInit: Boolean = PreferenceUtil.getInstance(applicationContext).getBoolean(INIT, true)
        when (isInit) {
            false -> toDataLoad()
            true -> {
                Realm.getDefaultInstance().executeTransaction { it.delete(Data::class.java) }

                Observable.zip(RestUtil.getHiinaData(),
                        RestUtil.getKurokumoData(),
                        BiFunction { hiina: List<Data>, kurokumo: List<Data> ->
                            DataUtil.initHiina(hiina)
                            DataUtil.initKurokumo(kurokumo)
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ toDataLoad() }, { Log.e(TAG, it.toString()) })
                        .apply { disposables.add(this) }

                PreferenceUtil.getInstance(applicationContext).setBoolean(INIT, false)
            }
        }
    }

    private fun toDataLoad() {
        val dataSet: List<ListFragment> = listOf(
                newInstance(R.string.utaite_hiina),
                newInstance(R.string.utaite_kurokumo),
                newInstance(R.string.utaite_nameless),
                newInstance(R.string.utaite_ayaponzu),
                newInstance(R.string.utaite_kuyuri)
        )

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

        mainViewPager.run {
            adapter = MainAdapter(supportFragmentManager, self, dataSet)
            currentItem = SettingUtil.TAB_MAX_VALUE / (2 * dataSet.size) * dataSet.size
        }

        val tab = mainTabLayout.getChildAt(0) as LinearLayout
        (0 until mainTabLayout.tabCount)
                .filter { tab.getChildAt(it) is LinearLayout }
                .map { tab.getChildAt(it) as LinearLayout }
                .filter { it.getChildAt(1) is TextView }
                .map { it.getChildAt(1) as TextView }
                .forEach { it.setFont(self) }
    }

}
