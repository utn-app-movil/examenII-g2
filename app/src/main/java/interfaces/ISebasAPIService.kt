package interfaces

import cr.ac.utn.appmovil.rooms.SebasApiResponse
import cr.ac.utn.appmovil.rooms.SebasAuthRequest
import cr.ac.utn.appmovil.rooms.SebasBookingRequest
import cr.ac.utn.appmovil.rooms.SebasCreateRoomRequest
import cr.ac.utn.appmovil.rooms.SebasGetAuthResponse
import cr.ac.utn.appmovil.rooms.SebasRoom
import cr.ac.utn.appmovil.rooms.SebasRoomListResponse
import cr.ac.utn.appmovil.rooms.SebasUnbookingRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ISebasAPIService {

    @POST("/users/auth")
    suspend fun authenticateUser(@Body request: SebasAuthRequest): SebasGetAuthResponse

    @GET("/users")
    suspend fun getUsers(): SebasApiResponse<List<Any>>

    @POST("/rooms")
    suspend fun createRoom(@Body request: SebasCreateRoomRequest): SebasApiResponse<Any>

    @GET("/rooms")
    suspend fun getRooms(): SebasRoomListResponse

    @GET("/rooms/{roomName}")
    suspend fun getRoomByName(@Path("roomName") roomName: String): SebasApiResponse<SebasRoom>

    @PUT("/rooms/booking")
    suspend fun bookRoom(@Body request: SebasBookingRequest): SebasApiResponse<Any>

    @PUT("/rooms/unbooking")
    suspend fun unbookRoom(@Body request: SebasUnbookingRequest): SebasApiResponse<Any>
}
