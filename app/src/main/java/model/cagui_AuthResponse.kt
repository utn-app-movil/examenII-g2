package model

data class cagui_AuthResponse(
    val data: cagui_User?,
    val responseCode: String,
    val message: String
)
