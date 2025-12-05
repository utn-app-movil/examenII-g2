package cr.ac.utn.appmovil.util

import cr.ac.utn.appmovil.rooms.kris_ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object kris_RetrofitClient {

    private val kris_baseUrl: String by lazy {
        val raw = util.util.apiURL
        if (raw.endsWith("/")) raw else "$raw/"
    }

    private val kris_okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    val kris_api: kris_ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(kris_baseUrl)
            .client(kris_okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(kris_ApiService::class.java)
    }
}
