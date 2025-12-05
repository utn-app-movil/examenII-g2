package tamanetwork

import model.tama_AuthRequest
import model.tama_AuthResponse
import model.tama_BaseResponse
import model.tama_BookingRequest
import model.tama_Room
import model.tama_User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface TamaApiService {

    @POST("users/auth")
    suspend fun tama_authUser(
        @Body tama_request: tama_AuthRequest
    ): Response<tama_BaseResponse<tama_User>>


    @GET("rooms")
    suspend fun tama_getRooms(): Response<tama_BaseResponse<List<tama_Room>>>


    @PUT("rooms/booking")
    suspend fun tama_bookRoom(
        @Body tama_request: tama_BookingRequest
    ): Response<tama_BaseResponse<Unit>>

    @PUT("rooms/unbooking")
    suspend fun tama_unbookRoom(
        @Body tama_request: tama_BookingRequest
    ): Response<tama_BaseResponse<Unit>>
}