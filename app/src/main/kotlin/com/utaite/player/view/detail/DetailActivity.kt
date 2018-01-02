package com.utaite.player.view.detail

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import com.utaite.player.rest.*
import com.utaite.player.util.ERROR
import com.utaite.player.util.IS_LYRICS
import com.utaite.player.util.PreferenceUtil
import com.utaite.player.util.setTitle
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_detail
    override val self = this@DetailActivity

    private val menuSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    override fun onBackPressed() {
        when (detailWebView.canGoBack()) {
            false -> super.onBackPressed()
            true -> detailWebView.goBack()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val isLyrics: Boolean = PreferenceUtil.getInstance(applicationContext).getBoolean(IS_LYRICS, true)
        menuSubject.onNext(isLyrics)
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        menuSubject.subscribe({
            when (resources.configuration.orientation) {
                android.content.res.Configuration.ORIENTATION_LANDSCAPE -> {
                    detailLyrics.visibility = View.GONE
                    menu.findItem(R.id.detailMenuLyrics).isVisible = false
                }
                android.content.res.Configuration.ORIENTATION_PORTRAIT -> {
                    val pair = when (it) {
                        false -> View.VISIBLE to android.R.drawable.ic_menu_close_clear_cancel
                        true -> View.GONE to android.R.drawable.ic_menu_more
                    }
                    detailLyrics.visibility = pair.first
                    menu.findItem(R.id.detailMenuLyrics).setIcon(pair.second)
                    menu.findItem(R.id.detailMenuLyrics).isVisible = true
                    PreferenceUtil.getInstance(applicationContext).setBoolean(IS_LYRICS, it)
                }
            }
        }, { Log.e(ERROR, it.toString()) })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.detailMenuLyrics) {
            val isLyrics: Boolean = PreferenceUtil.getInstance(applicationContext).getBoolean(IS_LYRICS, true)
            menuSubject.onNext(!isLyrics)
            return true
        }
        return false
    }

    override fun init() {
        supportActionBar?.setTitle(self, "${getString(intent.getIntExtra(UTAITE, 0))} - ${intent.getStringExtra(TITLE)}")

        detailWebView.run {
            loadUrl("http://embed.nicovideo.jp/watch/sm${intent.getStringExtra(WATCH)}")
            settings.run {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (!url.startsWith("http://embed.nicovideo.jp/watch/sm") && url.startsWith("http")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            addFlags(Intent.FLAG_FROM_BACKGROUND)
                        }
                        startActivity(intent)
                    }
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    detailProgressBar.visibility = View.GONE
                    detailWebView.visibility = View.VISIBLE

                    val isLyrics: Boolean = PreferenceUtil.getInstance(applicationContext).getBoolean(IS_LYRICS, true)
                    menuSubject.onNext(isLyrics)
                }
            }
        }
    }

}
