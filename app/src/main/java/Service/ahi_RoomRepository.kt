package Service

import Service.ahi_RetrofitClient
import model.*

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
                    true to "Bienvenido, ${body.data?.name ?: "Usuario"}"
                } else {
                    currentUserEmail = null
                    currentUserName = null
                    false to (body?.message ?: "Usuario o contraseña incorrectos")
                }
            } else {
                currentUserEmail = null
                currentUserName = null
                false to "Error del servidor: ${response.code()}"
            }
        } catch (e: Exception) {
            currentUserEmail = null
            currentUserName = null
            false to "Sin conexión a internet"
        }
    }

    suspend fun getRooms(): Pair<List<ahi_Room>?, String> {
        return try {
            val response = api.getRooms()
            if (response.isSuccessful && response.body()?.responseCode == "SUCESSFUL") {
                response.body()?.data to "OK"
            } else {
                null to (response.body()?.message ?: "Error al cargar salas")
            }
        } catch (e: Exception) {
            null to e.localizedMessage.orEmpty()
        }
    }

    suspend fun createRoom(roomName: String, capacity: Int): String {
        val request = ahi_CreateRoomRequest(roomName, capacity)
        return try {
            val resp = api.createRoom(request)
            resp.body()?.message ?: if (resp.isSuccessful) "Sala creada" else "Error"
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
                    body.message ?: "Acción ejecutada correctamente"
                } else {
                    body?.message ?: "Operación completada"
                }
            } else {
                resp.errorBody()?.string() ?: "Error ${resp.code()}"
            }
        } catch (e: Exception) {
            "Error de red: ${e.message}"
        }
    }

    suspend fun unbookRoom(room: String): String {
        val request = ahi_UnbookRequest(room)
        return try {
            val resp = api.unbookRoom(request)
            resp.body()?.message ?: if (resp.isSuccessful) "Liberada" else "Error"
        } catch (e: Exception) {
            e.localizedMessage.orEmpty()
        }
    }
}