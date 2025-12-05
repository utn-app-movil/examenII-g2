package cr.ac.utn.appmovil.rooms

import com.google.gson.annotations.SerializedName

data class SebasRoom(
    @SerializedName("room") val room: String,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("is_busy") val isBusy: Boolean,
    @SerializedName("user") val user: String,
    @SerializedName("date") val date: String?
)

data class SebasAuthRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class SebasGetAuthResponse(
    @SerializedName("data") val data: SebasUserData?,
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("message") val message: String
)

data class SebasUserData(
    @SerializedName("user") val user: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("emailname") val emailname: String
)

data class Room(
    @SerializedName("room") var room: String = "",
    @SerializedName("capacity") var capacity: Int = 0,
    @SerializedName("is_busy") var isBusy: Boolean = false,
    @SerializedName("user") var user: String = "",
    @SerializedName("date") var date: String = ""
)

data class User(
    @SerializedName("user") var user: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("lastname") var lastname: String = "",
    @SerializedName("emailname") var emailname: String = ""
)

data class SebasCreateRoomRequest(
    @SerializedName("room") val room: String,
    @SerializedName("capacity") val capacity: Int
)

data class SebasBookingRequest(
    @SerializedName("room") val room: String,
    @SerializedName("username") val username: String
)

data class SebasUnbookingRequest(
    @SerializedName("room") val room: String
)

data class SebasApiResponse<T>(
    @SerializedName("data") val data: T?,
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("message") val message: String
)

data class SebasRoomListResponse(
    @SerializedName("data") val data: List<SebasRoom>?,
    @SerializedName("responseCode") val responseCode: String,
    @SerializedName("message") val message: String
)
