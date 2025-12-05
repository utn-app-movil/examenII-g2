package cr.ac.utn.appmovil.rooms

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Walk_AuthService {

    @POST("users/auth")
    fun login(@Body body: Map<String, String>): Call<Walk_LoginResponse>
}
