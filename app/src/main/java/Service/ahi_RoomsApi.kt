package Service

import model.*
import retrofit2.Response
import retrofit2.http.*

interface ahi_RoomsApi {

    @POST("/users/auth")
    suspend fun authenticate(@Body request: ahi_AuthRequest): Response<ahi_ApiResponse<ahi_User>>

    @GET("/rooms")
    suspend fun getRooms(): Response<ahi_ApiResponse<List<ahi_Room>>>

    @POST("/rooms")
    suspend fun createRoom(@Body request: ahi_CreateRoomRequest): Response<ahi_ApiResponse<Any>>

    @PUT("/rooms/booking")
    suspend fun bookRoom(@Body request: ahi_BookRequest): Response<ahi_ApiResponse<Any>>

    @PUT("/rooms/unbooking")
    suspend fun unbookRoom(@Body request: ahi_UnbookRequest): Response<ahi_ApiResponse<Any>>
}