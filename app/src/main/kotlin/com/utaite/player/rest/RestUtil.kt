package com.utaite.player.rest

import com.utaite.player.BuildConfig
import com.utaite.player.data.Data
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


class RestUtil {

    companion object {

        private var data: Retrofit? = null

        private fun getDataInstance(): Retrofit {
            if (data == null) {
                synchronized(this) {
                    data = build("https://raw.githubusercontent.com/utaite/utaite-player/master/data/")
                }
            }

            return data as Retrofit
        }

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

        private fun build(url: String): Retrofit =
                Retrofit.Builder()
                        .baseUrl(url)
                        .client(getClient())
                        .addConverterFactory(GsonConverterFactory.create())
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

}
