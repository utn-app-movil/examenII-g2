package Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object mich_APIService {

    val api: mich_IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(mich_IAPIService::class.java)
    }
}
