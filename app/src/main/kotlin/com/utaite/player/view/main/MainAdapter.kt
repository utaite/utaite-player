package com.utaite.player.view.main

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.utaite.player.util.SettingUtil
import com.utaite.player.util.getUtaite
import com.utaite.player.util.newInstance
import com.utaite.player.view.list.ListFragment


class MainAdapter(fm: FragmentManager,
                  private val context: Context,
                  private val dataSet: List<ListFragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
            newInstance(dataSet.getUtaite(position))

    override fun getPageTitle(position: Int): String =
            context.getString(dataSet.getUtaite(position))

    override fun getCount(): Int =
            SettingUtil.TAB_MAX_VALUE

}
