package model

data class psan_userData(
    val user: String,
    val name: String,
    val lastname: String,
    val email: String
)

data class psan_userAuthResponse(
    val data: psan_userData?,
    val responseCode: String,
    val message: String
)
