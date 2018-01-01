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


const val NETWORK_ERROR = "NETWORK_ERROR"


class RestUtil {

    companion object {

        private var info: Retrofit? = null
        private var data: Retrofit? = null

        private fun getInfoInstance(): Retrofit {
            if (info == null) {
                synchronized(this) {
                    info = build(SimpleXmlConverterFactory.create(), "http://ext.nicovideo.jp/api/getthumbinfo/")
                }
            }

            return info as Retrofit
        }

        private fun getDataInstance(): Retrofit {
            if (data == null) {
                synchronized(this) {
                    data = build("https://raw.githubusercontent.com/utaite/utaite-player/master/data/")
                }
            }

            return data as Retrofit
        }

        fun getInfo(url: String): Observable<Info> =
                RestUtil.getInfoInstance()
                        .create(RestUtil.GetInfo::class.java)
                        .getInfo(url)

        fun getHiinaData(): Observable<List<Data>> =
                RestUtil.getDataInstance()
                        .create(RestUtil.HiinaData::class.java)
                        .getHiinaData()

        fun getKurokumoData(): Observable<List<Data>> =
                RestUtil.getDataInstance()
                        .create(RestUtil.KurokumoData::class.java)
                        .getKurokumoData()

        fun getNamelessData(): Observable<List<Data>> =
                RestUtil.getDataInstance()
                        .create(RestUtil.NamelessData::class.java)
                        .getNamelessData()

        fun getYuikonnuData(): Observable<List<Data>> =
                RestUtil.getDataInstance()
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
        @GET("sm{url}")
        fun getInfo(@Path("url") url: String
        ): Observable<Info>
    }

    interface HiinaData {
        @GET("HiinaData.json")
        fun getHiinaData(
        ): Observable<List<Data>>
    }

    interface KurokumoData {
        @GET("KurokumoData.json")
        fun getKurokumoData(
        ): Observable<List<Data>>
    }

    interface NamelessData {
        @GET("NamelessData.json")
        fun getNamelessData(
        ): Observable<List<Data>>
    }

    interface YuikonnuData {
        @GET("YuikonnuData.json")
        fun getYuikonnuData(
        ): Observable<List<Data>>
    }

}
