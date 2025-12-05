
package cr.ac.utn.appmovil.rooms

import cr.ac.utn.appmovil.util.kris_RetrofitClient
import cr.ac.utn.appmovil.rooms.kris_UserAuthRequest
import cr.ac.utn.appmovil.rooms.kris_UserAuthResponse
import retrofit2.Response

class kris_AuthRepository {

    suspend fun authenticate(username: String, password: String): Response<kris_UserAuthResponse> {
        val request = kris_UserAuthRequest(username, password)
        return kris_RetrofitClient.kris_api.kris_authenticateUser(request)

    }

}
