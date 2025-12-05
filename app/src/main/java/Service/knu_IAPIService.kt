package Service

import cr.ac.utn.appmovil.rooms.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface knu_IAPIService {

    // Authentication
    @POST("/users/auth")
    fun knu_login(@Body body: knu_LoginRequest): Call<knu_LoginResponse>

    // Rooms list
    @GET("/rooms")
    fun knu_getRooms(): Call<knu_RoomListResponse>

    // Single room by name
    @GET("/rooms/{roomName}")
    fun knu_getRoom(@Path("roomName") roomName: String): Call<knu_RoomResponse>

    // Create room
    @POST("/rooms")
    fun knu_createRoom(@Body body: knu_CreateRoomRequest): Call<knu_RoomResponse>

    // Book room - PUT según el enunciado
    @PUT("/rooms/booking")
    fun knu_bookRoom(@Body body: knu_BookRoomRequest): Call<knu_RoomResponse>

    // Unbook room - PUT según el enunciado
    @PUT("/rooms/unbooking")
    fun knu_unbookRoom(@Body body: knu_UnbookRoomRequest): Call<knu_RoomResponse>
}
