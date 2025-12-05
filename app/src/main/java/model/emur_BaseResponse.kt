package model

data class emur_BaseResponse<T>(
    val responseCode: String? = null,
    val message: String? = null,
    val data: T? = null
)
