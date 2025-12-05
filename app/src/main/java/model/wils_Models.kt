package model


data class wils_ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)


data class wils_User(
    val username: String = "",
    val password: String = "",
    val name: String = "",
    val isActive: Boolean = true,
    val lastname: String = "",
    val email: String = ""
)


data class wils_Room(
    val room: String = "",
    val capacity: Int = 0,
    val is_busy: Boolean = false,
    val user: String = "",
    val date: String? = null
)


data class wils_AuthRequest(
    val username: String,
    val password: String
)

data class wils_CreateRoomRequest(
    val room: String,
    val capacity: Int
)

data class wils_BookRoomRequest(
    val room: String,
    val username: String
)

data class wils_UnbookRoomRequest(
    val room: String
)