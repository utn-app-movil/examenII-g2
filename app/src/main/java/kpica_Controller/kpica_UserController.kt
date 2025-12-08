package kpica_Controller

import Service.kpica_APIService
import android.content.Context
import android.util.Log
import cr.ac.utn.appmovil.rooms.R
import kpica_Entity.kpica_DTOUser
import kpica_Entity.kpica_User

class kpica_UserController {
    private  var context: Context

    constructor(context: Context){
        this.context=context
    }

    suspend fun userLogin(userName: String, password: String) : kpica_User {
        var user = kpica_User()
        try {
            user.Username = userName
            user.Password = password

            val userDTO = convertToDTOUserObject(user)
            val response = kpica_APIService.apiUser.loginUser(userDTO)

            if (response.responseCode == context.getString(R.string.kpica_ResponseCorrect)) {
                user = convertToUserObjet(response.data)
                return user
            }
            else {
                throw Exception("${context
                    .getString(R.string.kpica_ErrorLogin)} ${response.message}")
            }
        } catch (e: Exception){
            Log.e("API_Call", "Error fetching data: ${e.message}")
            throw Exception(context
                .getString(R.string.kpica_ErrorLogin))
        }
        return user
    }

    private fun convertToUserObjet(item: kpica_DTOUser): kpica_User {
        val user = kpica_User()
        user.Username = item.UserName
        user.Name = item.Name
        user.LastName = item.LastName
        user.Email = item.Email

        return user
    }

    private fun convertToDTOUserObject (user: kpica_User): kpica_DTOUser {
        return kpica_DTOUser(user.Username, user.Password, user.Name,
            user.IsActive, user.LastName, user.Email)
    }
}