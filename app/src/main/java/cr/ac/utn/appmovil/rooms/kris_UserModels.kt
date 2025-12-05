package cr.ac.utn.appmovil.rooms


data class kris_ApiResponse<T>(
    val data: T? = null,
    val responseCode: String,
    val message: String
)


data class kris_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String,
    val date: String?
)

data class kris_RoomCreateRequest(
    val room: String,
    val capacity: Int
)

data class kris_RoomBookingRequest(
    val room: String,
    val username: String
)

data class kris_RoomUnbookingRequest(
    val room: String
)


data class kris_UserAuthRequest(
    val username: String,
    val password: String
)

data class kris_UserAuthResponse(
    val data: kris_UserData? = null,
    val responseCode: String,
    val message: String
)

data class kris_UserData(
    val username: String?,
    val name: String?,
    val lastname: String?,
    val email: String?,
    val isActive: Boolean? = null
)
