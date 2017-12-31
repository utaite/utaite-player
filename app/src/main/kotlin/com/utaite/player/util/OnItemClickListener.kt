package com.utaite.player.util


interface OnItemClickListener<in T> {

    fun onItemClick(position: Int, item: T)

}
