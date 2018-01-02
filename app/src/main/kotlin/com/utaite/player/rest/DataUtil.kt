package com.utaite.player.rest

import com.utaite.player.R
import io.realm.Realm


const val INDEX = "index"
const val UTAITE = "utaite"
const val TITLE = "title"
const val URL = "url"
const val COUNT = "count"


class DataUtil {

    companion object {

        private fun init(dataSet: List<Data>) =
                Realm.getDefaultInstance().executeTransaction { it.setDataSet(dataSet) }

        fun initAyaponzu(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_ayaponzu } })

        fun initHiina(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_hiina } })

        fun initKurokumo(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_kurokumo } })

        fun initLailai(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_lailai } })

        fun initNameless(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_nameless } })

        fun initRibonnu(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_ribonnu } })

        fun initWotamin(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_wotamin } })

        fun initYuikonnu(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_yuikonnu } })

        private fun Realm.setDataSet(dataSet: List<Data>, isAutoIndex: Boolean = true) =
                dataSet.forEach {
                    val index = when (isAutoIndex) {
                        false -> it.index
                        true -> where(Data::class.java).max(INDEX)?.toInt()?.plus(1) ?: 0
                    }

                    createObject(Data::class.java, index).run {
                        utaite = it.utaite
                        title = it.title
                        url = it.url
                    }
                }

    }

}
