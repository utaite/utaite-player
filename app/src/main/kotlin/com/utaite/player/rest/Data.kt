package com.utaite.player.rest

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Data(
        @PrimaryKey var index: Int = 0,
        var utaite: Int = 0,
        var title: String = "",
        var url: String = "",
        var count: Int = 0
) : RealmObject()
