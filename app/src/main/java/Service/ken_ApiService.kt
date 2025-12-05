package Service

import model.ken_ApiRespone
import model.ken_room
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ken_ApiService {

    @POST("/users/auth")
    suspend fun login(@Body body: Map<String, String>): ken_ApiRespone<Unit>


    @GET("/rooms")
    suspend fun getRooms(): ken_ApiRespone<List<ken_room>>

    @GET("/rooms/{roomName}")
    suspend fun getRoom(@Path("roomName") roomName: String): ken_ApiRespone<ken_room>

    @POST("/rooms")
    suspend fun createRoom(@Body room: ken_room): ken_ApiRespone<Unit>

    @PUT("/rooms/booking")
    suspend fun bookRoom(@Body payload: Map<String, String>): ken_ApiRespone<Unit>

    @PUT("/rooms/unbooking")
    suspend fun unbookRoom(@Body payload: Map<String, String>): ken_ApiRespone<Unit>
}
