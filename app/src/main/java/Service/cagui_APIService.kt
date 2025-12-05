package Service

import interfaces.cagui_IAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object cagui_APIService {

    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val api: cagui_IAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(cagui_IAPIService::class.java)
    }
}
