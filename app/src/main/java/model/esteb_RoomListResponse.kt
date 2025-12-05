package model

data class esteb_RoomListResponse(
    val data: List<esteb_Room>,
    val responseCode: String,
    val message: String
)
