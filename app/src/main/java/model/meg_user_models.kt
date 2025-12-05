package com.utn.rooms.model

data class MegUserAuthRequest(
    val user: String,
    val password: String
)

data class MegUser(
    val user: String,
    val name: String,
    val lastname: String,
    val emailname: String
)

data class MegApiResponse<T>(
    val data: T?,
    val responseCode: String,
    val message: String
)
data class MegRoom(
    val id: String,
    val name: String,
    val capacity: Int,
    val reserved: Boolean
)

data class MegRoomCreateRequest(
    val name: String,
    val capacity: Int
)

data class MegRoomBookingRequest(
    val roomId: String
)
