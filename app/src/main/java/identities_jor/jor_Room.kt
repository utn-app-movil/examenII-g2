package cr.ac.utn.appmovil.rooms.identities

data class jor_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String = ""
)