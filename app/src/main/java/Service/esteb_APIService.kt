package Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object esteb_APIService {

    val api: esteb_IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(esteb_IAPIService::class.java)
    }
}
