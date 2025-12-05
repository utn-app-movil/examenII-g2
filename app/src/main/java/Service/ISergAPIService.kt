package cr.ac.utn.appmovil.rooms.service

import cr.ac.utn.appmovil.rooms.model.*
import retrofit2.Call
import retrofit2.http.*

interface ISergAPIService {

    @POST("/users/auth")
    fun authenticateUser(@Body authRequest: SergAuthRequest): Call<SergApiResponse<SergUser>>

    @GET("/users")
    fun getAllUsers(): Call<SergApiResponse<List<SergUser>>>

    @POST("/rooms")
    fun createRoom(@Body roomRequest: SergCreateRoomRequest): Call<SergApiResponse<SergRoom>>

    @GET("/rooms")
    fun getAllRooms(): Call<SergApiResponse<List<SergRoom>>>

    @PUT("/rooms/booking")
    fun bookRoom(@Body bookingRequest: SergBookingRequest): Call<SergApiResponse<SergRoom>>

    @PUT("/rooms/unbooking")
    fun unbookRoom(@Body unbookingRequest: SergBookingRequest): Call<SergApiResponse<SergRoom>>
}