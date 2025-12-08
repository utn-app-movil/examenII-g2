package kpica_Entity

data class kpica_UserResponse(
        val data: kpica_DTOUser,
        val message: String,
        val responseCode: String
    )