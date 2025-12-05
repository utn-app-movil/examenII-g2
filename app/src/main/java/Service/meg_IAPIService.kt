package com.utn.rooms.Service

import com.utn.rooms.model.*
import retrofit2.http.*

interface meg_IAPIService {

    @POST("users/auth")
    suspend fun auth(@Body body: MegUserAuthRequest): MegApiResponse<MegUser>

    @GET("users")
    suspend fun getUsers(): MegApiResponse<List<MegUser>>

    @GET("rooms")
    suspend fun getRooms(): MegApiResponse<List<MegRoom>>

    @POST("rooms")
    suspend fun createRoom(@Body body: MegRoomCreateRequest): MegApiResponse<MegRoom>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body body: MegRoomBookingRequest): MegApiResponse<MegRoom>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body body: MegRoomBookingRequest): MegApiResponse<MegRoom>
}
