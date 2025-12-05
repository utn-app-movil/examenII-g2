package cr.ac.utn.appmovil.rooms.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object SergAPIService {
    val api: ISergAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ISergAPIService::class.java)
    }
}