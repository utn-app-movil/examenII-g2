package cr.ac.utn.appmovil.rooms

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Walk_ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Walk_Constants.BASE_URL)   // URL base del API del examen
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Servicios del API
    val authService: Walk_AuthService = retrofit.create(Walk_AuthService::class.java)
    val roomService: Walk_RoomService = retrofit.create(Walk_RoomService::class.java)
    val userService: Walk_UserService = retrofit.create(Walk_UserService::class.java) // 🔥 Nuevo servicio para obtener email
}
