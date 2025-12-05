package com.utn.rooms.Service


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object meg_APIService {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val api: meg_IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(meg_IAPIService::class.java)
    }
}
