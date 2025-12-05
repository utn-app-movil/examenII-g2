package Service

import model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface emur_IAPIService {

    // 1. Autenticación (POST /users/auth)
    @POST("/users/auth")
    suspend fun authenticateUser(@Body authRequest: emur_AuthRequest):
            Response<emur_BaseResponse<emur_AuthData>>

    // 2. Creación de Salas (POST /rooms)
    @POST("/rooms")
    suspend fun createRoom(@Body roomRequest: emur_CreateRoomRequest):
            Response<emur_BaseResponse<Any?>>

    // 3. Obtener Salas (GET /rooms)
    @GET("/rooms")
    suspend fun getRooms():
            Response<emur_BaseResponse<List<emur_Room>>>

    // 4. Reserva de Sala (PUT /rooms/booking)
    @PUT("/rooms/booking")
    suspend fun bookRoom(@Body bookingRequest: emur_BookingRequest):
            Response<emur_BaseResponse<Any?>>

    // 5. Liberación de Sala (PUT /rooms/unbooking)
    @PUT("/rooms/unbooking")
    suspend fun unbookRoom(@Body unbookingRequest: emur_UnbookingRequest):
            Response<emur_BaseResponse<Any?>>
}