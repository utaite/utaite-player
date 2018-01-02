package com.utaite.player.view.list

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import com.utaite.player.R
import com.utaite.player.base.BaseFragment
import com.utaite.player.rest.*
import com.utaite.player.util.OnItemClickListener
import com.utaite.player.util.PreferenceUtil
import com.utaite.player.util.IS_SORTED
import com.utaite.player.util.SettingUtil
import com.utaite.player.view.detail.DetailActivity
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.Sort
import kotlinx.android.synthetic.main.fragment_list.*


private const val SORTED_NEWEST_UPLOAD = 0
private const val SORTED_MOST_VIEW = 1
private const val SORTED_TITLE = 2


class ListFragment : BaseFragment(), OnItemClickListener<Data> {

    override val layoutId: Int = R.layout.fragment_list
    override val self = this@ListFragment

    override fun init() {
        Realm.getDefaultInstance().executeTransaction {
            val sortedIndex: Int = PreferenceUtil.getInstance(activity.applicationContext).getInt(IS_SORTED, 0)
            val dataSet: MutableList<Data> = when (sortedIndex) {
                SORTED_NEWEST_UPLOAD -> it.getDataSet().findAllSorted(INDEX, Sort.DESCENDING)
                SORTED_MOST_VIEW -> it.getDataSet().findAllSorted(COUNT, Sort.DESCENDING)
                SORTED_TITLE -> it.getDataSet().findAllSorted(TITLE)
                else -> it.getDataSet().findAll()
            }.toMutableList()

            listRecyclerView.run {
                layoutManager = GridLayoutManager(activity, SettingUtil.RECYCLER_SPAN_COUNT)
                adapter = ListAdapter(activity, dataSet, self)
            }
        }
    }

    private fun Realm.getDataSet(): RealmQuery<Data> =
            where(Data::class.java).equalTo(UTAITE, arguments.getInt(UTAITE))

    override fun onItemClick(position: Int, item: Data) {
        val intent: Intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(UTAITE, item.utaite)
            putExtra(TITLE, item.title)
            putExtra(WATCH, item.watch)
        }
        startActivity(intent)
    }

}
