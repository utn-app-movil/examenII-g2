package cr.ac.utn.appmovil.rooms.model

data class SergBookingRequest(
    val room: String,
    val username: String? = null
)