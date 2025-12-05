package model

data class esteb_AuthResponse(
    val data: UserData?,
    val responseCode: String?,
    val message: String?
)

data class UserData(
    val user: String,
    val name: String,
    val lastname: String,
    val email: String
)