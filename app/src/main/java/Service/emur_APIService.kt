package Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object emur_APIService {


    val emur_apiRooms: emur_IAPIService by lazy {
        Retrofit.Builder()

            .baseUrl(util.Companion.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(emur_IAPIService::class.java)
    }

}