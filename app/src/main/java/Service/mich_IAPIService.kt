package Service

import model.mich_AuthRequest
import model.mich_AuthResponse
import model.mich_RoomCreateRequest
import model.mich_RoomResponse
import model.mich_RoomBookingRequest
import model.mich_RoomBookingResponse
import model.mich_RoomListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT

interface mich_IAPIService {

    @POST("/users/auth")
    fun mich_authUser(@Body auth: mich_AuthRequest): Call<mich_AuthResponse>

    @POST("/rooms")
    fun mich_createRoom(@Body req: mich_RoomCreateRequest): Call<mich_RoomResponse>

    @GET("/rooms")
    fun mich_getRooms(): Call<mich_RoomListResponse>

    @PUT("/rooms/booking")
    fun mich_booking(@Body req: mich_RoomBookingRequest): Call<mich_RoomBookingResponse>

    @PUT("/rooms/unbooking")
    fun mich_unbooking(@Body req: mich_RoomBookingRequest): Call<mich_RoomBookingResponse>
}


