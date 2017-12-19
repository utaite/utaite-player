package com.utaite.player.view.list

import android.support.v7.widget.GridLayoutManager
import com.utaite.player.R
import com.utaite.player.base.BaseFragment
import com.utaite.player.data.Data
import com.utaite.player.data.UTAITE
import com.utaite.player.util.SettingUtil
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_list

    override fun init() {
        Realm.getDefaultInstance().executeTransaction {
            val dataSet: MutableList<Data> = it.where(Data::class.java).equalTo(UTAITE, arguments.getInt(UTAITE)).findAll()

            listRecyclerView.run {
                layoutManager = GridLayoutManager(activity, SettingUtil.RECYCLER_SPAN_COUNT)
                adapter = ListAdapter(activity, dataSet)
            }
        }
    }

}
