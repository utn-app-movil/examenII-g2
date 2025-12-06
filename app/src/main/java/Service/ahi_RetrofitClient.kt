package Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util.Companion.apiURL

object ahi_RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(apiURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ahi_RoomsApi = retrofit.create(ahi_RoomsApi::class.java)
}