package service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object ken_ApiClient {

    val api: ken_ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ken_ApiService::class.java)
    }
}
