package com.utaite.player.rest

import android.util.Log
import com.utaite.player.R
import com.utaite.player.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm


const val INDEX = "index"
const val UTAITE = "utaite"
const val TITLE = "title"
const val URL = "url"
const val COUNT = "count"


class DataUtil {

    companion object {

        private fun BaseActivity.init(dataSet: List<Data>) {
            Realm.getDefaultInstance().executeTransaction { it.setDataSet(dataSet) }
            Observable.fromIterable(dataSet)
                    .flatMap { RestUtil.getInfo(it.url) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ info ->
                        Realm.getDefaultInstance().executeTransaction {
                            val url: String = info.thumb.watchUrl.run { substring(indexOf("sm") + 2) }
                            val data: Data? = it.where(Data::class.java).equalTo(URL, url).findFirst()
                            data?.count = info.thumb.viewCounter.toInt()
                        }
                    }, { Log.e(NETWORK_ERROR, it.toString()) })
                    .apply { disposables.add(this) }
        }

        fun BaseActivity.initHiina(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_hiina } })

        fun BaseActivity.initKurokumo(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_kurokumo } })

        fun BaseActivity.initNameless(dataSet: List<Data>) =
                init(dataSet.map { it.apply { utaite = R.string.utaite_nameless } })

        fun BaseActivity.initYuikonnu(dataSet: List<Data>) =
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
                        count = it.count
                    }
                }

    }

}
