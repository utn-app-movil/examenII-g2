package interfaces

import kpica_Entity.kpica_DTOUser
import kpica_Entity.kpica_UserResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface kpica_IUserAPIService {
    @Headers("Content-Type: application/json")
    @POST("/users/auth")
    suspend fun loginUser (@Body user: kpica_DTOUser): kpica_UserResponse
}