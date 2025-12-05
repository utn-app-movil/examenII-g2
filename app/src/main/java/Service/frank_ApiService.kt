package service

import interfaces.frank_IApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object frank_ApiService {
    val instance: frank_IApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(frank_IApiService::class.java)
    }
}