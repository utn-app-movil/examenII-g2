package model

data class ahi_ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)