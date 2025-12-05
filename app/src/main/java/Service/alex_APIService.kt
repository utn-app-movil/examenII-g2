package cr.ac.utn.appmovil.rooms

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object alex_APIService {
    val api: alex_IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(alex_IAPIService::class.java)
    }
}