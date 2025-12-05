package model

data class mich_AuthResponse(
    val data: mich_User?,
    val responseCode: String,
    val message: String
)