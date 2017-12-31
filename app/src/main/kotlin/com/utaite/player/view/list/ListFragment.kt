package com.utaite.player.view.list

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import com.utaite.player.R
import com.utaite.player.base.BaseFragment
import com.utaite.player.data.Data
import com.utaite.player.data.TITLE
import com.utaite.player.data.URL
import com.utaite.player.data.UTAITE
import com.utaite.player.util.OnItemClickListener
import com.utaite.player.util.SettingUtil
import com.utaite.player.view.detail.DetailActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : BaseFragment(), OnItemClickListener<Data> {

    override val layoutId: Int = R.layout.fragment_list
    override val self = this@ListFragment

    override fun init() {
        Realm.getDefaultInstance().executeTransaction {
            val dataSet: MutableList<Data> = it.where(Data::class.java).equalTo(UTAITE, arguments.getInt(UTAITE)).findAll()

            listRecyclerView.run {
                layoutManager = GridLayoutManager(activity, SettingUtil.RECYCLER_SPAN_COUNT)
                adapter = ListAdapter(activity, dataSet, self)
            }
        }
    }

    override fun onItemClick(position: Int, item: Data) {
        val intent: Intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(UTAITE, item.utaite)
            putExtra(TITLE, item.title)
            putExtra(URL, item.url)
        }
        startActivity(intent)
    }

}
