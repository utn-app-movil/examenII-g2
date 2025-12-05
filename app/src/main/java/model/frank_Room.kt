package model

data class frank_Room(
    val room: String,
    val capacity: String, // Changed from Int to String to handle inconsistent API data
    val is_busy: Boolean,
    val user: String,
    val date: String?
)