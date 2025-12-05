package cr.ac.utn.appmovil.rooms.model

data class SergApiResponse<T>(
    val data: T? = null,
    val responseCode: String,
    val message: String
)