package cr.ac.utn.appmovil.rooms

data class alex_AuthRequest(val username: String, val password: String)
data class alex_CreateRoomRequest(val room: String, val capacity: Int)
data class alex_BookingRequest(val room: String, val username: String)
data class alex_UnbookingRequest(val room: String)

data class alex_User(val user: String, val name: String, val lastname: String, val email: String)
data class alex_Room(val room: String, val capacity: Int, val is_busy: Boolean, val user: String?, val date: String?)

data class alex_AuthResponse(
    val data: alex_User?,
    val responseCode: String,
    val message: String
)

data class alex_RoomResponse(
    val data: List<alex_Room>?,
    val responseCode: String,
    val message: String
)

data class alex_GenericResponse(
    val data: Any?,
    val responseCode: String,
    val message: String
)