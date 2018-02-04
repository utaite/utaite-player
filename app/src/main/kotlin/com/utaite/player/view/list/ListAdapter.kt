package com.utaite.player.view.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.utaite.player.R
import com.utaite.player.rest.Data
import com.utaite.player.util.getView
import kotlinx.android.synthetic.main.fragment_list_view.view.*


class ListAdapter(private val dataSet: MutableList<Data>,
                  private val listener: OnDataClickListener<Data>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(getView(parent.context, R.layout.fragment_list_view)).apply {
                itemView.listViewImage.setOnClickListener { listener.onDataClick(dataSet[adapterPosition]) }
                itemView.listViewTitle.setOnClickListener { listener.onDataClick(dataSet[adapterPosition]) }
            }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            listener.onBindViewHolder(holder, dataSet[position])

    override fun getItemCount(): Int
            = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
