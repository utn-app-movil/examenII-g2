package Service

import model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface wils_ApiInterface {
    @POST("users/auth")
    suspend fun authenticateUser(@Body authRequest: wils_AuthRequest): wils_ApiResponse<Any>
    
    @GET("users")
    suspend fun getUsers(): wils_ApiResponse<List<wils_User>>
    
    @GET("rooms")
    suspend fun getRooms(): wils_ApiResponse<List<wils_Room>>
    
    @POST("rooms")
    suspend fun createRoom(@Body createRoomRequest: wils_CreateRoomRequest): wils_ApiResponse<Any>
    
    @PUT("rooms/booking")
    suspend fun bookRoom(@Body bookRoomRequest: wils_BookRoomRequest): wils_ApiResponse<Any>
    
    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body unbookRoomRequest: wils_UnbookRoomRequest): wils_ApiResponse<Any>
}

class wils_APIService {
    
    private val baseUrl = "https://rooms-api.azurewebsites.net/"
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        
    private val apiInterface = retrofit.create(wils_ApiInterface::class.java)
    

    suspend fun authenticateUser(authRequest: wils_AuthRequest): wils_ApiResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.authenticateUser(authRequest)
            } catch (e: Exception) {
                wils_ApiResponse(null, "ERROR", "Network error: ${e.message}")
            }
        }
    }
    

    suspend fun getUsers(): wils_ApiResponse<List<wils_User>> {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.getUsers()
            } catch (e: Exception) {
                wils_ApiResponse(null, "ERROR", "Network error: ${e.message}")
            }
        }
    }
    

    suspend fun getRooms(): wils_ApiResponse<List<wils_Room>> {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.getRooms()
            } catch (e: Exception) {
                wils_ApiResponse(null, "ERROR", "Network error: ${e.message}")
            }
        }
    }
    
    suspend fun createRoom(createRoomRequest: wils_CreateRoomRequest): wils_ApiResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.createRoom(createRoomRequest)
            } catch (e: Exception) {
                wils_ApiResponse(null, "ERROR", "Network error: ${e.message}")
            }
        }
    }
    
    suspend fun bookRoom(bookRoomRequest: wils_BookRoomRequest): wils_ApiResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.bookRoom(bookRoomRequest)
            } catch (e: Exception) {
                wils_ApiResponse(null, "ERROR", "Network error: ${e.message}")
            }
        }
    }
    
    suspend fun unbookRoom(unbookRoomRequest: wils_UnbookRoomRequest): wils_ApiResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                apiInterface.unbookRoom(unbookRoomRequest)
            } catch (e: Exception) {
                wils_ApiResponse(null, "ERROR", "Network error: ${e.message}")
            }
        }
    }
}