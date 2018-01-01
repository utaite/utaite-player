package com.utaite.player.util

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.utaite.player.R
import com.utaite.player.rest.UTAITE
import com.utaite.player.view.list.ListFragment
import kotlinx.android.synthetic.main.common_action_bar.view.*


fun getView(context: Context, resId: Int): View =
        LayoutInflater.from(context).inflate(resId, null)

fun TextView.setFont(context: Context) {
    typeface = Typeface.createFromAsset(context.assets, SettingUtil.FONT)
}

fun ActionBar.setTitle(context: Context, title: Int) =
        setTitle(context, context.getString(title))

fun ActionBar.setTitle(context: Context, title: String) {
    customView = getView(context, R.layout.common_action_bar).apply {
        actionBarTitle.text = title
        actionBarTitle.setFont(context)
    }
    displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
}

fun newListInstance(utaite: Int): ListFragment =
        ListFragment().apply {
            arguments = Bundle().apply {
                putInt(UTAITE, utaite)
            }
        }

fun List<ListFragment>.getUtaite(position: Int): Int =
        this[position % size].arguments.getInt(UTAITE)
