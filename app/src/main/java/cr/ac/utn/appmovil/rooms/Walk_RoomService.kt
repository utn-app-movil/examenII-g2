package cr.ac.utn.appmovil.rooms

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface Walk_RoomService {

    @GET("rooms")
    fun getRooms(): Call<Walk_ApiResponse<List<Walk_Room>>>

    @POST("rooms")
    fun createRoom(@Body body: Map<String, Any>): Call<Walk_ApiResponse<Any>>

    @PUT("rooms/booking")
    fun bookRoom(@Body body: Map<String, String>): Call<Walk_ApiResponse<Any>>

    @PUT("rooms/unbooking")
    fun unbookRoom(@Body body: Map<String, String>): Call<Walk_ApiResponse<Any>>
}
