package model

data class mich_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val start: String?,
    val end: String?
)
