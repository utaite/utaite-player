package com.utaite.player.view.list

import android.support.v7.widget.RecyclerView


interface OnDataClickListener<in T> {

    fun onDataClick(data: T)

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: T)

}
