package sebas_controller

import android.content.Context
import android.util.Log
import cr.ac.utn.appmovil.rooms.SebasAuthRequest
import cr.ac.utn.appmovil.rooms.SebasGetAuthResponse
import cr.ac.utn.appmovil.rooms.SebasUserData
import cr.ac.utn.appmovil.rooms.User
import util.sebas_APIservice

class UserController {
    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    suspend fun authenticateUser(sebasAuthRequest: SebasAuthRequest): SebasGetAuthResponse {
        val response = sebas_APIservice.apiRoom.authenticateUser(sebasAuthRequest)
        Log.d("API_Call", "Auth response: ${response.responseCode} - ${response.message}")
        return response
    }

    suspend fun getUser(): List<User> {
        var user = mutableListOf<User>()
        try {
            val response = sebas_APIservice.apiRoom.getUsers()
            if (response.responseCode != "SUCESSFUL" && response.responseCode != "INFO_FOUND")
                throw Exception(response.message)

            response.data?.forEach { item ->
                user.add(getUserObject(item as SebasUserData))
            }
            Log.d("API_Call", "Success: ${response.data}")
        } catch (e: Exception) {
            Log.e("API_Call", "Error fetching data: ${e.message}")
            throw Exception(e.message)
        }
        return user
    }

    private fun getUserObject(item: SebasUserData): User {
        val user = User()
        user.user = item.user
        user.name = item.name
        user.lastname = item.lastname
        user.emailname = item.emailname
        return user
    }
}