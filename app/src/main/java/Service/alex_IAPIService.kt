package cr.ac.utn.appmovil.rooms

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface alex_IAPIService {
    @POST("/users/auth")
    fun authUser(@Body request: alex_AuthRequest): Call<alex_AuthResponse>

    @GET("/rooms")
    fun getRooms(): Call<alex_RoomResponse>

    @POST("/rooms")
    fun createRoom(@Body request: alex_CreateRoomRequest): Call<alex_GenericResponse>

    @PUT("/rooms/booking")
    fun bookRoom(@Body request: alex_BookingRequest): Call<alex_GenericResponse>

    @PUT("/rooms/unbooking")
    fun unbookRoom(@Body request: alex_UnbookingRequest): Call<alex_GenericResponse>
}