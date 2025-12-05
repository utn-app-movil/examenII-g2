package cr.ac.utn.appmovil.rooms.model

data class carl_ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)