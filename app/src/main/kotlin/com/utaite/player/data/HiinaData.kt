package com.utaite.player.data

import com.utaite.player.R
import io.realm.Realm


class HiinaData {

    companion object {

        fun init() {
            val realm = Realm.getDefaultInstance()
            val dataSet: List<Data> = listOf(
                    Data(utaite = R.string.utaite_hiina, title = R.string.title_moment, url = "30662327"),
                    Data(utaite = R.string.utaite_hiina, title = R.string.title_rapunzel, url = "29200132"),
                    Data(utaite = R.string.utaite_hiina, title = R.string.title_nandemonaiya, url = "29894591"),
                    Data(utaite = R.string.utaite_hiina, title = R.string.title_shiroyuki, url = "28422184")
            )
            realm.setDataSet(dataSet)
        }

    }

}
