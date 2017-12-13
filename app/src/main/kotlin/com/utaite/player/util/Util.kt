package com.utaite.player.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.ActionBar
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.utaite.player.R
import com.utaite.player.app.FONT
import kotlinx.android.synthetic.main.common_action_bar.view.*


fun getView(context: Context, resId: Int): View =
        LayoutInflater.from(context).inflate(resId, null)

fun Bitmap.getDominantColor(): Int {
    val resize = Bitmap.createScaledBitmap(this, 1, 1, true)
    val color = resize.getPixel(0, 0)
    resize.recycle()
    return color
}

fun Int.getOppositionColor(): Int =
        Color.rgb(255 - Color.red(this), 255 - Color.green(this), 255 - Color.blue(this))

fun TextView.setFont(context: Context) {
    typeface = Typeface.createFromAsset(context.assets, FONT)
}

fun ActionBar.setTitle(context: Context, title: Int) {
    customView = getView(context, R.layout.common_action_bar).apply {
        actionBarTitle.text = context.getString(title)
        actionBarTitle.setFont(context)
    }
    displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
}
