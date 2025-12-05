package model

data class frank_ApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)