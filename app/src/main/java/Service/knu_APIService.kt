package Service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import util.util

object knu_APIService {

    val api: knu_IAPIService by lazy {
        Retrofit.Builder()
            // Usa la misma URL base que ya trae el proyecto :contentReference[oaicite:2]{index=2}
            .baseUrl(util.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(knu_IAPIService::class.java)
    }
}
