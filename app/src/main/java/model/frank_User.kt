package model

data class frank_User(
    val username: String,
    val password: String,
    val name: String,
    val lastname: String,
    val email: String,
    val isActive: Boolean
)