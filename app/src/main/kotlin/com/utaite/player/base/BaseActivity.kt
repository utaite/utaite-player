package com.utaite.player.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutId: Int
    protected abstract val self: Context

    val disposables by lazy { CompositeDisposable() }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        init()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    abstract fun init()

}
