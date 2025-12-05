package cr.ac.utn.appmovil.rooms

import retrofit2.Call
import retrofit2.http.GET

interface Walk_UserService {

    @GET("users")
    fun getUsers(): Call<Walk_ApiResponse<List<Walk_User>>>

}