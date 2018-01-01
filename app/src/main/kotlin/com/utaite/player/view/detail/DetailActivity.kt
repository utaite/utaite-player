package com.utaite.player.view.detail

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import com.utaite.player.rest.TITLE
import com.utaite.player.rest.URL
import com.utaite.player.rest.UTAITE
import com.utaite.player.util.setTitle
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_detail
    override val self = this@DetailActivity

    override fun onBackPressed() {
        when (detailWebView.canGoBack()) {
            false -> super.onBackPressed()
            true -> detailWebView.goBack()
        }
    }

    override fun init() {
        supportActionBar?.setTitle(self, "${getString(intent.getIntExtra(UTAITE, 0))} - ${intent.getStringExtra(TITLE)}")

        detailWebView.run {
            loadUrl("http://embed.nicovideo.jp/watch/sm${intent.getStringExtra(URL)}")
            settings.run {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    detailProgressBar.visibility = View.GONE
                    detailWebView.visibility = View.VISIBLE
                }
            }
        }
    }

}
