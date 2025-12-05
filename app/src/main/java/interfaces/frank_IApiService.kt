package interfaces

import model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface frank_IApiService {
    @POST("users/auth")
    fun auth(@Body authRequest: frank_AuthRequest): Call<frank_ApiResponse<Unit>>

    @POST("rooms")
    fun createRoom(@Body createRoomRequest: frank_CreateRoomRequest): Call<frank_ApiResponse<Unit>>

    @GET("rooms")
    fun getRooms(): Call<frank_ApiResponse<List<frank_Room>>>

    @PUT("rooms/booking")
    fun bookRoom(@Body bookRequest: frank_BookingRequest): Call<frank_ApiResponse<Unit>>

    @PUT("rooms/unbooking")
    fun unbookRoom(@Body unbookRequest: frank_UnbookingRequest): Call<frank_ApiResponse<Unit>>

    @GET("users")
    fun getUsers(): Call<frank_ApiResponse<List<frank_User>>>
}
