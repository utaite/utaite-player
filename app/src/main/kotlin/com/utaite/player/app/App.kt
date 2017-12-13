package com.utaite.player.app

import android.app.Application
import com.utaite.player.R
import io.realm.Realm
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


const val FONT = "font/NanumGothic.ttf"


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val calligraphyConfig = CalligraphyConfig.Builder()
                .setDefaultFontPath(FONT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        CalligraphyConfig.initDefault(calligraphyConfig)
    }

}
