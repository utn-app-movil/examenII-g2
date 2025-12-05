package model

data class esteb_RoomResponse(
    val message: String?,
    val data: RoomData?
)

data class RoomData(
    val id: Int,
    val room: String,
    val capacity: Int
)
