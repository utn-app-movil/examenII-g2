package tamanetwork

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object TamaApiClient {


    private val tama_baseUrl: String =
        if (util.apiURL.endsWith("/")) util.apiURL else util.apiURL + "/"

    init {

        Log.d("TamaApiClient", "BASE URL = $tama_baseUrl")
    }

    val tama_retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(tama_baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}