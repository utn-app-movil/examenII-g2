package model

data class fer_ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)
