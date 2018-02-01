package com.utaite.player.view.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import com.utaite.player.rest.*
import com.utaite.player.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        menuSubject
                .subscribe({
                    when (resources.configuration.orientation) {
                        android.content.res.Configuration.ORIENTATION_LANDSCAPE -> {
                            detailLayout.visibility = View.GONE
                            menu.findItem(R.id.detailMenuLyrics).isVisible = false
                        }
                        android.content.res.Configuration.ORIENTATION_PORTRAIT -> {
                            val pair = when (it) {
                                false -> View.GONE to android.R.drawable.ic_menu_more
                                true -> View.VISIBLE to android.R.drawable.ic_menu_revert
                            }
                            detailLayout.visibility = pair.first
                            menu.findItem(R.id.detailMenuLyrics).setIcon(pair.second)
                            menu.findItem(R.id.detailMenuLyrics).isVisible = true
                            PreferenceUtil.getInstance(applicationContext).setBoolean(IS_LYRICS, it)
                        }
                    }
                }, {
                    Log.e(ERROR, it.toString())
                    finish()
                })
                .apply { disposables.add(this) }
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

        when (networkCheck()) {
            false -> {
                detailProgressBar.visibility = View.GONE
                ToastUtil.getInstance(applicationContext).text(self, R.string.detail_network_error)
            }
            true -> {
                RestUtil.getLyrics(intent.getStringExtra(TITLE))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            detailLyrics.text = it.string()
                            detailLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                        }, {
                            Log.e(NETWORK_ERROR, it.toString())
                            detailLyrics.layoutParams = (detailLyrics.layoutParams as FrameLayout.LayoutParams).apply { gravity = Gravity.CENTER }
                            detailLyrics.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                            detailLyrics.text = getString(R.string.detail_lyrics_load_failed)
                        })
                        .apply { disposables.add(this) }

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
    }

    private fun networkCheck(): Boolean =
            (self.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null

}
