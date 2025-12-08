package interfaces

import kpica_Entity.kpica_DTORoom
import kpica_Entity.kpica_DTORoom_Booking
import kpica_Entity.kpica_DTORoom_Insert
import kpica_Entity.kpica_DTORoom_UnBooking
import kpica_Entity.kpica_RoomGetResponse
import kpica_Entity.kpica_RoomResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.*

interface kpica_IRoomAPIService {
    @GET("/rooms")
    suspend fun getAll(): kpica_RoomGetResponse

    @Headers("Content-Type: application/json")
    @POST("/rooms")
    suspend fun createRoom (@Body room: kpica_DTORoom_Insert): kpica_RoomResponse

    @Headers("Content-Type: application/json")
    @PUT("/rooms/booking")
    suspend fun updateBookingRoom (@Body room: kpica_DTORoom_Booking): kpica_RoomResponse

    @Headers("Content-Type: application/json")
    @PUT("/rooms/unbooking")
    suspend fun updateUnBookingRoom (@Body room: kpica_DTORoom_UnBooking): kpica_RoomResponse
}