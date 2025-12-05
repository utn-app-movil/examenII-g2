package model

data class tama_BaseResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)