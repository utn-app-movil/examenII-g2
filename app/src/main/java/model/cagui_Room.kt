package model

import com.google.gson.annotations.SerializedName

data class cagui_Room(
    val room: String,
    val capacity: Int,
    @SerializedName("is_busy")
    val isBusy: Boolean,
    val user: String?,
    val date: String?
)
