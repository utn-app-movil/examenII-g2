package cr.ac.utn.appmovil.rooms

data class Walk_User(
    val username: String,
    val name: String,
    val lastname: String,
    val email: String,
    val isActive: Boolean
)

data class Walk_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String,
    val date: String?
)

data class Walk_ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)

data class Walk_LoginData(
    val user: String,
    val name: String,
    val lastname: String,
    val email: String
)

data class Walk_LoginResponse(
    val data: Walk_LoginData?,
    val responseCode: String,
    val message: String
)
