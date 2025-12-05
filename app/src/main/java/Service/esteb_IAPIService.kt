package Service

import retrofit2.http.Body
import retrofit2.http.POST
import model.esteb_AuthRequest
import model.esteb_AuthResponse
import model.esteb_RoomListResponse
import model.esteb_RoomRequest
import model.esteb_RoomResponse
import retrofit2.http.GET

interface esteb_IAPIService {

    @POST("/users/auth")
    suspend fun login(@Body request: esteb_AuthRequest): esteb_AuthResponse
    @POST("/rooms")
    suspend fun createRoom(@Body request: esteb_RoomRequest): esteb_RoomResponse

    @GET("/rooms")
    suspend fun getRooms(): esteb_RoomListResponse

}