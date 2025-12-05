package cr.ac.utn.appmovil.rooms.model

import com.google.gson.annotations.SerializedName

data class carl_Room(
    @SerializedName("room")
    val room: String,

    @SerializedName("capacity")
    val capacity: Int,

    @SerializedName("is_busy")
    val isBusy: Boolean,

    @SerializedName("user")
    val user: String?,

    @SerializedName("date")
    val date: String?
)