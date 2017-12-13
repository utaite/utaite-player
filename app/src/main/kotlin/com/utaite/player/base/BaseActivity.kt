package com.utaite.player.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutId: Int
    protected abstract val self: Context

    val TAG: String = javaClass.simpleName

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        init()
    }

    abstract fun init()

}
