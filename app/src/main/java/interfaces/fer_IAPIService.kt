package interfaces

import model.*
import retrofit2.Response
import retrofit2.http.*

interface fer_IAPIService {

    @POST("/users/auth")
    suspend fun authenticateUser(@Body request: fer_AuthRequest): Response<fer_ApiResponse<fer_AuthData>>

    @GET("/users")
    suspend fun getUsers(): Response<fer_ApiResponse<List<fer_User>>>

    @POST("/rooms")
    suspend fun createRoom(@Body request: fer_RoomRequest): Response<fer_ApiResponse<Any>>

    @GET("/rooms")
    suspend fun getRooms(): Response<fer_ApiResponse<List<fer_Room>>>

    @GET("/rooms/{roomName}")
    suspend fun getRoomByName(@Path("roomName") roomName: String): Response<fer_ApiResponse<fer_Room>>

    @PUT("/rooms/booking")
    suspend fun bookRoom(@Body request: fer_BookingRequest): Response<fer_ApiResponse<Any>>

    @PUT("/rooms/unbooking")
    suspend fun unbookRoom(@Body request: fer_UnbookingRequest): Response<fer_ApiResponse<Any>>
}
