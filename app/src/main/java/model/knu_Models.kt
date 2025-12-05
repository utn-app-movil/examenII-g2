package cr.ac.utn.appmovil.rooms.model

// ====== LOGIN ======

data class knu_LoginRequest(
    val username: String,
    val password: String
)

data class knu_LoginData(
    val user: String,
    val name: String,
    val lastname: String,
    val email: String
)

data class knu_LoginResponse(
    val data: knu_LoginData?,
    val responseCode: String,
    val message: String
)

// ====== ROOMS ======

data class knu_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val date: String?
)

data class knu_RoomListResponse(
    val data: List<knu_Room>?,
    val responseCode: String,
    val message: String
)

data class knu_RoomResponse(
    val data: knu_Room?,
    val responseCode: String,
    val message: String
)

// ====== REQUESTS ROOMS ======

data class knu_CreateRoomRequest(
    val room: String,
    val capacity: Int
)

data class knu_BookRoomRequest(
    val username: String,
    val room: String
)

data class knu_UnbookRoomRequest(
    val room: String
)
