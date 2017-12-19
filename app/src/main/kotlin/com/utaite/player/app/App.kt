package com.utaite.player.app

import android.app.Application
import com.utaite.player.R
import com.utaite.player.util.SettingUtil
import io.realm.Realm
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val calligraphyConfig = CalligraphyConfig.Builder()
                .setDefaultFontPath(SettingUtil.FONT)
                .setFontAttrId(R.attr.fontPath)
                .build()
        CalligraphyConfig.initDefault(calligraphyConfig)
    }

}
