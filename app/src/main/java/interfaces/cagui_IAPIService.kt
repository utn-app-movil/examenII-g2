package interfaces

import model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface cagui_IAPIService {

    @POST("users/auth")
    suspend fun authenticate(@Body authRequest: cagui_AuthRequest): cagui_AuthResponse

    @GET("users/")
    suspend fun getUsers(): List<cagui_User>

    @GET("rooms")
    suspend fun getRooms(): cagui_RoomResponse

    @POST("rooms")
    suspend fun createRoom(@Body roomRequest: cagui_RoomCreateRequest): cagui_RoomCreateResponse

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body bookingRequest: cagui_RoomBookingRequest): cagui_RoomBookingResponse

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body bookingRequest: cagui_RoomBookingRequest): cagui_RoomBookingResponse
}
