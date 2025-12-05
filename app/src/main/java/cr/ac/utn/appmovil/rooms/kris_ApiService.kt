package cr.ac.utn.appmovil.rooms

import retrofit2.Response
import retrofit2.http.*

interface kris_ApiService {



    @POST("/rooms")
    suspend fun kris_createRoom(@Body kris_body: kris_RoomCreateRequest): Response<kris_ApiResponse<kris_Room>>

    @GET("/rooms")
    suspend fun kris_getRooms(): Response<kris_ApiResponse<List<kris_Room>>>

    @GET("/rooms/{kris_roomName}")
    suspend fun kris_getRoomByName(@Path("kris_roomName") kris_roomName: String): Response<kris_ApiResponse<kris_Room>>

    @PUT("/rooms/booking")
    suspend fun kris_bookRoom(@Body kris_body: kris_RoomBookingRequest): Response<kris_ApiResponse<Any>>

    @PUT("/rooms/unbooking")
    suspend fun kris_unbookRoom(@Body kris_body: kris_RoomUnbookingRequest): Response<kris_ApiResponse<Any>>

    // --- USERS ---
    @POST("/users/auth")
    suspend fun kris_authenticateUser(@Body kris_body: kris_UserAuthRequest): Response<kris_UserAuthResponse>

    @GET("/users")
    suspend fun kris_getUsers(): Response<kris_ApiResponse<List<kris_UserData>>>
}
