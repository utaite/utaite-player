package com.utaite.player.rest

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "nicovideo_thumb_response ", strict = false)
class Info {

    @set:Element(name = "thumb")
    @get:Element(name = "thumb")
    var thumb: Thumb = Thumb()

}

@Root(name = "thumb ", strict = false)
class Thumb {

    @set:Element(name = "video_id")
    @get:Element(name = "video_id")
    var videoId: String = ""

    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String = ""

    @set:Element(name = "description")
    @get:Element(name = "description")
    var description: String = ""

    @set:Element(name = "thumbnail_url")
    @get:Element(name = "thumbnail_url")
    var thumbnailUrl: String = ""

    @set:Element(name = "first_retrieve")
    @get:Element(name = "first_retrieve")
    var firstRetrieve: String = ""

    @set:Element(name = "length")
    @get:Element(name = "length")
    var length: String = ""

    @set:Element(name = "movie_type")
    @get:Element(name = "movie_type")
    var movieType: String = ""

    @set:Element(name = "size_high")
    @get:Element(name = "size_high")
    var sizeHigh: String = ""

    @set:Element(name = "size_low")
    @get:Element(name = "size_low")
    var sizeLow: String = ""

    @set:Element(name = "view_counter")
    @get:Element(name = "view_counter")
    var viewCounter: String = ""

    @set:Element(name = "comment_num")
    @get:Element(name = "comment_num")
    var commentNum: String = ""

    @set:Element(name = "mylist_counter")
    @get:Element(name = "mylist_counter")
    var mylistCounter: String = ""

    @set:Element(name = "last_res_body")
    @get:Element(name = "last_res_body")
    var lastResBody: String = ""

    @set:Element(name = "watch_url")
    @get:Element(name = "watch_url")
    var watchUrl: String = ""

    @set:Element(name = "thumb_type")
    @get:Element(name = "thumb_type")
    var thumbType: String = ""

    @set:Element(name = "embeddable")
    @get:Element(name = "embeddable")
    var embeddable: String = ""

    @set:Element(name = "no_live_play")
    @get:Element(name = "no_live_play")
    var noLivePlay: String = ""

    @set:Element(name = "user_id")
    @get:Element(name = "user_id")
    var userId: String = ""

    @set:Element(name = "user_nickname")
    @get:Element(name = "user_nickname")
    var userNickname: String = ""

    @set:Element(name = "user_icon_url")
    @get:Element(name = "user_icon_url")
    var userIconUrl: String = ""

}
