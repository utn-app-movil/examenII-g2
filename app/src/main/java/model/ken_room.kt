package model

import com.google.gson.annotations.SerializedName

class ken_room() {

    @SerializedName("room")
    var room: String = ""

    @SerializedName("capacity")
    var capacity: Int = 0

    @SerializedName("is_busy")
    var isBusy: Boolean = false

    @SerializedName("user")
    var user: String? = null

    @SerializedName("date")
    var date: String? = null

    constructor(
        room: String,
        capacity: Int,
        isBusy: Boolean,
        user: String?,
        date: String?
    ) : this() {
        this.room = room
        this.capacity = capacity
        this.isBusy = isBusy
        this.user = user
        this.date = date
    }
}
