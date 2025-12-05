package model

data class emur_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val date: String?
)

data class emur_AuthData(
    val user: String? = null,
    val token: String? = null
)

