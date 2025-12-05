package model

data class ken_ApiRespone<T>(
    val message: String,
    val responseCode: Int,
    val data: T?
)
