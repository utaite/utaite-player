package com.utaite.player.rest

import com.utaite.player.BuildConfig
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


class RestUtil {

    companion object {

        private var info: Retrofit? = null
        private var resource: Retrofit? = null

        private fun getInfoInstance(): Retrofit {
            if (info == null) {
                synchronized(this) {
                    info = build(SimpleXmlConverterFactory.create(), "http://ext.nicovideo.jp/api/getthumbinfo/")
                }
            }

            return info as Retrofit
        }

        private fun getResourceInstance(): Retrofit {
            if (resource == null) {
                synchronized(this) {
                    resource = build("https://raw.githubusercontent.com/utaite/utaite-player/master/")
                }
            }

            return resource as Retrofit
        }

        fun getInfo(watch: String): Observable<Info> =
                RestUtil.getInfoInstance()
                        .create(RestUtil.GetInfo::class.java)
                        .getInfo(watch)

        fun getLyrics(title: String): Observable<ResponseBody> =
                RestUtil.getInfoInstance()
                        .create(RestUtil.GetLyrics::class.java)
                        .getLyrics(title)

        fun getAyaponzuData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.AyaponzuData::class.java)
                        .getAyaponzuData()

        fun getHiinaData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.HiinaData::class.java)
                        .getHiinaData()

        fun getKurokumoData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.KurokumoData::class.java)
                        .getKurokumoData()

        fun getLaiLaiData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.LaiLaiData::class.java)
                        .getLaiLaiData()

        fun getNamelessData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.NamelessData::class.java)
                        .getNamelessData()

        fun getRibonnuData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.RibonnuData::class.java)
                        .getRibonnuData()

        fun getWotaminData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.WotaminData::class.java)
                        .getWotaminData()

        fun getYuikonnuData(): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.YuikonnuData::class.java)
                        .getYuikonnuData()

        private fun build(url: String): Retrofit =
                build(GsonConverterFactory.create(), url)

        private fun build(converter: Converter.Factory, url: String): Retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .client(getClient())
                        .addConverterFactory(converter)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                        .build()

        private fun getClient(): OkHttpClient =
                OkHttpClient.Builder()
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100, TimeUnit.SECONDS)
                        .writeTimeout(100, TimeUnit.SECONDS)
                        .followRedirects(false)
                        .apply {
                            if (BuildConfig.DEBUG) {
                                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                            }
                        }
                        .build()

    }

    interface GetInfo {
        @GET("sm{watch}")
        fun getInfo(@Path("watch") watch: String
        ): Observable<Info>
    }

    interface GetLyrics {
        @GET("lyrics/{title}.txt")
        fun getLyrics(@Path("title") title: String
        ): Observable<ResponseBody>
    }

    interface AyaponzuData {
        @GET("data/AyaponzuData.json")
        fun getAyaponzuData(
        ): Observable<List<Data>>
    }

    interface HiinaData {
        @GET("data/HiinaData.json")
        fun getHiinaData(
        ): Observable<List<Data>>
    }

    interface KurokumoData {
        @GET("data/KurokumoData.json")
        fun getKurokumoData(
        ): Observable<List<Data>>
    }

    interface LaiLaiData {
        @GET("data/LaiLaiData.json")
        fun getLaiLaiData(
        ): Observable<List<Data>>
    }

    interface NamelessData {
        @GET("data/NamelessData.json")
        fun getNamelessData(
        ): Observable<List<Data>>
    }

    interface RibonnuData {
        @GET("data/RibonnuData.json")
        fun getRibonnuData(
        ): Observable<List<Data>>
    }

    interface WotaminData {
        @GET("data/WotaminData.json")
        fun getWotaminData(
        ): Observable<List<Data>>
    }

    interface YuikonnuData {
        @GET("data/YuikonnuData.json")
        fun getYuikonnuData(
        ): Observable<List<Data>>
    }

}
