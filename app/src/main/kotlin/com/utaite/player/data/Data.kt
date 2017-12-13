package com.utaite.player.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Data(
        @PrimaryKey var index: Int = 0,
        var utaite: Int = 0,
        var title: Int = 0,
        var url: String = ""
) : RealmObject()
