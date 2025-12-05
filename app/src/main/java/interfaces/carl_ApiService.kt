package cr.ac.utn.appmovil.rooms.interfaces

import cr.ac.utn.appmovil.rooms.model.carl_ApiResponse
import cr.ac.utn.appmovil.rooms.model.carl_User
import cr.ac.utn.appmovil.rooms.model.carl_LoginRequest
import cr.ac.utn.appmovil.rooms.model.carl_Room
import cr.ac.utn.appmovil.rooms.model.carl_CreateRoomRequest
import cr.ac.utn.appmovil.rooms.model.carl_BookingRequest
import retrofit2.Response
import retrofit2.http.*

interface carl_ApiService {

    @POST("users/auth")
    suspend fun login(@Body request: carl_LoginRequest): Response<carl_ApiResponse<carl_User>>

    @GET("users")
    suspend fun getUsers(): Response<carl_ApiResponse<List<carl_User>>>

    @GET("rooms")
    suspend fun getRooms(): Response<carl_ApiResponse<List<carl_Room>>>

    @GET("rooms/{roomName}")
    suspend fun getRoom(@Path("roomName") roomName: String): Response<carl_ApiResponse<carl_Room>>

    @POST("rooms")
    suspend fun createRoom(@Body request: carl_CreateRoomRequest): Response<carl_ApiResponse<Any>>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: carl_BookingRequest): Response<carl_ApiResponse<Any>>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: carl_BookingRequest): Response<carl_ApiResponse<Any>>
}