// interfaces/jor_IAPIService.kt
package cr.ac.utn.appmovil.rooms.interfaces

import retrofit2.Call
import retrofit2.http.*

interface jor_IAPIService {
    @POST("users/auth")
    fun login(@Body body: Map<String, String>): Call<Any>

    @GET("rooms")
    fun getRooms(): Call<Any>

    @POST("rooms")
    fun createRoom(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<Any>

    @PUT("rooms/booking")
    fun bookRoom(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<Any>

    @PUT("rooms/unbooking")
    fun unbookRoom(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<Any>

    @GET("users")
    fun getUsers(): Call<Any>
}