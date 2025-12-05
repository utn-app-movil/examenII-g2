package cr.ac.utn.appmovil.rooms.kris_repository
import cr.ac.utn.appmovil.rooms.kris_ApiResponse
import cr.ac.utn.appmovil.rooms.kris_Room
import cr.ac.utn.appmovil.rooms.kris_RoomBookingRequest
import cr.ac.utn.appmovil.rooms.kris_RoomCreateRequest
import cr.ac.utn.appmovil.rooms.kris_RoomUnbookingRequest

import cr.ac.utn.appmovil.util.kris_RetrofitClient


import retrofit2.Response

class kris_RoomRepository {
    suspend fun createRoom(name: String, capacity: Int): Response<kris_ApiResponse<kris_Room>> {
        val kris_req = kris_RoomCreateRequest(room = name, capacity = capacity)
        return kris_RetrofitClient.kris_api.kris_createRoom(kris_req)
    }
    suspend fun getRooms(): Response<kris_ApiResponse<List<kris_Room>>> {
        return kris_RetrofitClient.kris_api.kris_getRooms()
    }


    suspend fun bookRoom(room: String, username: String): Response<kris_ApiResponse<Any>> {
        val kris_req = kris_RoomBookingRequest(room = room, username = username)
        return kris_RetrofitClient.kris_api.kris_bookRoom(kris_req)
    }


    suspend fun unbookRoom(room: String): Response<kris_ApiResponse<Any>> {
        val kris_req = kris_RoomUnbookingRequest(room = room)
        return kris_RetrofitClient.kris_api.kris_unbookRoom(kris_req)
    }
}
