package model

data class psan_roomListResponse(
    val data: List<psan_room>?,
    val responseCode: String,
    val message: String
)
