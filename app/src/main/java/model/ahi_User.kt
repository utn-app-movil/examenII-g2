package model

data class ahi_User(
    val user: String,
    val name: String,
    val lastname: String,
    val emailname: String
)

data class ahi_AuthRequest(
    val username: String,
    val password: String
)

data class ahi_BookRequest(
    val room: String,
    val username: String
)

data class ahi_UnbookRequest(
    val room: String
)

data class ahi_CreateRoomRequest(
    val room: String,
    val capacity: Int
)