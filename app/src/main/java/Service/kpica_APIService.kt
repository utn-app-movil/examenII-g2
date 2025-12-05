package Service

import interfaces.kpica_IUserAPIService
import interfaces.kpica_IRoomAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object kpica_APIService {
    private var BASE_URL = util.util.apiURL + "/"

    val apiUser: kpica_IUserAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(kpica_IUserAPIService::class.java)
    }

    val apiRoom: kpica_IRoomAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(kpica_IRoomAPIService::class.java)
    }
}