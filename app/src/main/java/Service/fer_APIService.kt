package Service

import interfaces.fer_IAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object fer_APIService {

    val apiService: fer_IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.Companion.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(fer_IAPIService::class.java)
    }
}
