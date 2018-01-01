package com.utaite.player.view.detail

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

    override fun init() {
        supportActionBar?.setTitle(self, "${getString(intent.getIntExtra(UTAITE, 0))} - ${intent.getStringExtra(TITLE)}")

        detailWebView.run {
            settings.run {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            loadUrl("http://embed.nicovideo.jp/watch/sm${intent.getStringExtra(URL)}")
        }
    }

}
