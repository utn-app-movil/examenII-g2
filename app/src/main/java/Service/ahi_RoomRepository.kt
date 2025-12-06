package Service

import Service.ahi_RetrofitClient
import model.*
import android.content.res.Resources
import cr.ac.utn.appmovil.rooms.R

class ahi_RoomRepository {

    private val api = ahi_RetrofitClient.apiService

    var currentUserEmail: String? = null
    var currentUserName: String? = null

    suspend fun login(username: String, password: String): Pair<Boolean, String> {
        val request = ahi_AuthRequest(username, password)
        return try {
            val response = api.authenticate(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.responseCode == "SUCESSFUL") {
                    currentUserEmail = body.data?.emailname
                    currentUserName = body.data?.name?.let { "$it ${body.data?.lastname ?: ""}" }
                    true to String.format(Resources.getSystem().getString(R.string.ahi_login_welcome), body.data?.name ?: "Usuario")
                } else {
                    currentUserEmail = null
                    currentUserName = null
                    false to (body?.message ?: Resources.getSystem().getString(R.string.ahi_login_error))
                }
            } else {
                currentUserEmail = null
                currentUserName = null
                false to String.format(Resources.getSystem().getString(R.string.ahi_login_server_error), response.code())
            }
        } catch (e: Exception) {
            currentUserEmail = null
            currentUserName = null
            false to Resources.getSystem().getString(R.string.ahi_no_internet)
        }
    }

    suspend fun getRooms(): Pair<List<ahi_Room>?, String> {
        return try {
            val response = api.getRooms()
            if (response.isSuccessful && response.body()?.responseCode == "SUCESSFUL") {
                response.body()?.data to "OK"
            } else {
                null to (response.body()?.message ?: Resources.getSystem().getString(R.string.ahi_room_load_error))
            }
        } catch (e: Exception) {
            null to e.localizedMessage.orEmpty()
        }
    }

    suspend fun createRoom(roomName: String, capacity: Int): String {
        val request = ahi_CreateRoomRequest(roomName, capacity)
        return try {
            val resp = api.createRoom(request)
            resp.body()?.message ?: if (resp.isSuccessful) {
                Resources.getSystem().getString(R.string.ahi_room_created)
            } else {
                Resources.getSystem().getString(R.string.ahi_room_create_error)
            }
        } catch (e: Exception) {
            e.localizedMessage.orEmpty()
        }
    }

    suspend fun bookRoom(room: String, username: String): String {
        val request = ahi_BookRequest(room, username)
        return try {
            val resp = api.bookRoom(request)
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body?.responseCode == "SUCESSFUL" || body?.responseCode == "INFO_FOUND") {
                    body.message ?: Resources.getSystem().getString(R.string.ahi_room_booking_success)
                } else {
                    body?.message ?: Resources.getSystem().getString(R.string.ahi_room_booking_completed)
                }
            } else {
                resp.errorBody()?.string() ?: String.format(Resources.getSystem().getString(R.string.ahi_room_booking_error_code), resp.code())
            }
        } catch (e: Exception) {
            String.format(Resources.getSystem().getString(R.string.ahi_room_network_error), e.message)
        }
    }

    suspend fun unbookRoom(room: String): String {
        val request = ahi_UnbookRequest(room)
        return try {
            val resp = api.unbookRoom(request)
            resp.body()?.message ?: if (resp.isSuccessful) {
                Resources.getSystem().getString(R.string.ahi_room_released)
            } else {
                Resources.getSystem().getString(R.string.ahi_room_release_error)
            }
        } catch (e: Exception) {
            e.localizedMessage.orEmpty()
        }
    }
}