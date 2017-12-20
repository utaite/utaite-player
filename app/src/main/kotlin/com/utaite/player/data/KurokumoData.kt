package com.utaite.player.data

import com.utaite.player.R
import com.utaite.player.rest.RestUtil
import io.realm.Realm


class KurokumoData {

    companion object {

        fun init() {
            RestUtil.getKurokumoData().subscribe {
                val realm = Realm.getDefaultInstance()
                val dataSet: MutableList<Data> = mutableListOf()
                it.forEach {
                    dataSet.add(newData(it.title, it.url))
                }
                realm.setDataSet(dataSet)
            }
        }

        private fun newData(title: String, url: String): Data =
                Data(utaite = R.string.utaite_kurokumo, title = title, url = url)

    }

}
