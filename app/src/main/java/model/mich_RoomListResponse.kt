package model

data class mich_RoomListResponse(
    val data: List<mich_Room>?,
    val message: String,
    val responseCode: String
)
