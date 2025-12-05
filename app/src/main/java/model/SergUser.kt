package cr.ac.utn.appmovil.rooms.model

data class SergUser(
    val username: String,
    val password: String,
    val name: String,
    val lastname: String,
    val email: String,
    val isActive: Boolean
)