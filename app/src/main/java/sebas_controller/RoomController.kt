package sebas_controller

import android.content.Context
import android.util.Log
import cr.ac.utn.appmovil.rooms.SebasApiResponse
import cr.ac.utn.appmovil.rooms.SebasBookingRequest
import cr.ac.utn.appmovil.rooms.SebasCreateRoomRequest
import cr.ac.utn.appmovil.rooms.SebasRoom
import cr.ac.utn.appmovil.rooms.SebasUnbookingRequest
import util.sebas_APIservice

class RoomController {
    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    suspend fun createRoom(roomName: String, capacity: Int): SebasApiResponse<Any> {
        try {
            val request = SebasCreateRoomRequest(roomName, capacity)
            val response = sebas_APIservice.apiRoom.createRoom(request)
            if (response.responseCode != "SUCESSFUL" && response.responseCode != "INFO_FOUND")
                throw Exception(response.message)
            return response
        } catch (e: Exception) {
            Log.e("API_Call", "Error creating room: ${e.message}")
            throw Exception(e.message)
        }
    }

    suspend fun getRooms(): List<SebasRoom> {
        try {
            val response = sebas_APIservice.apiRoom.getRooms()
            if (response.responseCode != "SUCESSFUL" && response.responseCode != "INFO_FOUND")
                throw Exception(response.message)
            
            return response.data ?: emptyList()
        } catch (e: Exception) {
            Log.e("API_Call", "Error fetching rooms: ${e.message}")
            throw Exception(e.message)
        }
    }

    suspend fun bookRoom(roomName: String, username: String) {
        try {
            val request = SebasBookingRequest(roomName, username)
            val response = sebas_APIservice.apiRoom.bookRoom(request)
            if (response.responseCode != "SUCESSFUL" && response.responseCode != "INFO_FOUND")
                throw Exception(response.message)
        } catch (e: Exception) {
            Log.e("API_Call", "Error booking room: ${e.message}")
            throw Exception(e.message)
        }
    }

    suspend fun unbookRoom(roomName: String) {
        try {
            val request = SebasUnbookingRequest(roomName)
            val response = sebas_APIservice.apiRoom.unbookRoom(request)
            if (response.responseCode != "SUCESSFUL" && response.responseCode != "INFO_FOUND")
                throw Exception(response.message)
        } catch (e: Exception) {
            Log.e("API_Call", "Error unbooking room: ${e.message}")
            throw Exception(e.message)
        }
    }
}