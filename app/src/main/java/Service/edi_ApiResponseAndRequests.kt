package Service

data class edi_ApiResponse<T>(val data: T?,
                              val responseCode: String,
                              val message: String)
