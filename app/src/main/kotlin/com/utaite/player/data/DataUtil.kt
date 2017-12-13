package com.utaite.player.data

import io.realm.Realm


const val INDEX = "index"
const val UTAITE = "utaite"
const val TITLE = "title"
const val URL = "url"


fun Realm.setDataSet(dataSet: List<Data>, isAutoIndex: Boolean = true) =
        executeTransaction {
            dataSet.forEach {
                val index = when (isAutoIndex) {
                    false -> it.index
                    true -> where(Data::class.java).max(INDEX)?.let {
                        it.toInt() + 1
                    } ?: 0
                }

                createObject(Data::class.java, index).run {
                    utaite = it.utaite
                    title = it.title
                    url = it.url
                }
            }
        }
