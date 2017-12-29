package com.utaite.player.data

import com.utaite.player.R
import io.realm.Realm


const val INDEX = "index"
const val UTAITE = "utaite"
const val TITLE = "title"
const val URL = "url"


class DataUtil {

    companion object {

        private fun init(addDataList: (MutableList<Data>) -> Unit) {
            val realm = Realm.getDefaultInstance()
            val dataSet: MutableList<Data> = mutableListOf()
            addDataList(dataSet)
            realm.setDataSet(dataSet)
        }

        fun initHiina(dataList: List<Data>) =
                init { dataSet -> dataList.forEach { dataSet.add(setHiinaData(it)) } }

        fun initKurokumo(dataList: List<Data>) =
                init { dataSet -> dataList.forEach { dataSet.add(setKurokumoData(it)) } }

        fun initNameless(dataList: List<Data>) =
                init { dataSet -> dataList.forEach { dataSet.add(setNamelessData(it)) } }

        fun initYuikonnu(dataList: List<Data>) =
                init { dataSet -> dataList.forEach { dataSet.add(setYuikonnuData(it)) } }

        private fun Realm.setDataSet(dataSet: List<Data>, isAutoIndex: Boolean = true) =
                executeTransaction {
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

        private fun setHiinaData(data: Data): Data =
                Data(utaite = R.string.utaite_hiina, title = data.title, url = data.url)

        private fun setKurokumoData(data: Data): Data =
                Data(utaite = R.string.utaite_kurokumo, title = data.title, url = data.url)

        private fun setNamelessData(data: Data): Data =
                Data(utaite = R.string.utaite_nameless, title = data.title, url = data.url)

        private fun setYuikonnuData(data: Data): Data =
                Data(utaite = R.string.utaite_yuikonnu, title = data.title, url = data.url)

    }

}
