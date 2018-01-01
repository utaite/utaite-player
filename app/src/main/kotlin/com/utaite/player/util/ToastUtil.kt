package com.utaite.player.util

import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast


class ToastUtil private constructor(context: Context) : Toast(context) {

    companion object {

        private var instance: ToastUtil? = null

        fun getInstance(context: Context): ToastUtil {
            synchronized(this) {
                if (instance == null) {
                    instance = ToastUtil(context.applicationContext)
                }
            }

            return instance as ToastUtil
        }

    }

    private val toast by lazy {
        makeText(context, "", Toast.LENGTH_SHORT).apply {
            val view = view as LinearLayout
            (0 until view.childCount)
                    .map { view.getChildAt(it) }
                    .filter { it is TextView }
                    .map { it as TextView }
                    .forEach { it.typeface = Typeface.createFromAsset(context.assets, SettingUtil.FONT) }
        }
    }

    fun text(message: CharSequence) = setTextShow(message)
    fun text(context: Context, resId: Int) = setTextShow(context.getString(resId))

    private fun setTextShow(s: CharSequence) {
        toast.setText(s)
        toast.show()
    }

}
