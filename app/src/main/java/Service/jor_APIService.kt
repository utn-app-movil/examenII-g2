package cr.ac.utn.appmovil.rooms.service

import cr.ac.utn.appmovil.rooms.interfaces.jor_IAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object jor_APIService {

    val instance: jor_IAPIService = Retrofit.Builder()
        .baseUrl("https://rooms-api.azurewebsites.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(jor_IAPIService::class.java)
}