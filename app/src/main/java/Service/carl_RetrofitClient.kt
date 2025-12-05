package cr.ac.utn.appmovil.rooms.Service

import cr.ac.utn.appmovil.rooms.interfaces.carl_ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object carl_RetrofitClient {

    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"  // URL correcta

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: carl_ApiService by lazy {
        retrofit.create(carl_ApiService::class.java)
    }
}