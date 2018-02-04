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
                RestUtil.getResourceInstance()
                        .create(RestUtil.GetLyrics::class.java)
                        .getLyrics(title)

        fun getInformation(): Observable<Information> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.GetInformation::class.java)
                        .getInformation()

        fun getUtaiteData(utaite: String): Observable<List<Data>> =
                RestUtil.getResourceInstance()
                        .create(RestUtil.UtaiteData::class.java)
                        .getUtaiteData("${utaite}Data")

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

    interface GetInformation {
        @GET("Information.json")
        fun getInformation(
        ): Observable<Information>
    }

    interface UtaiteData {
        @GET("data/{utaite}.json")
        fun getUtaiteData(@Path("utaite") utaite: String
        ): Observable<List<Data>>
    }

}
