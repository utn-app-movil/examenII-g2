package Service

import model.*
import retrofit2.Response
import retrofit2.http.*

interface psan_IAPIService {

    // LOGIN
    @POST("/users/auth")
    suspend fun psan_authUser(
        @Body request: psan_userAuthRequest
    ): Response<psan_userAuthResponse>

    // ROOMS
    @GET("/rooms")
    suspend fun psan_getRooms(): Response<psan_roomListResponse>

    @POST("/rooms")
    suspend fun psan_createRoom(@Body request: psan_roomCreateRequest): Response<psan_userAuthResponse>

    @PUT("/rooms/booking")
    suspend fun psan_bookRoom(@Body request: psan_roomBookingRequest): Response<psan_userAuthResponse>

    @PUT("/rooms/unbooking")
    suspend fun psan_unbookRoom(@Body request: psan_roomBookingRequest): Response<psan_userAuthResponse>
}
