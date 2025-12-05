package kpica_Entity

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class kpica_DTORoom(
    @SerializedName("room") val Room: String,
    @SerializedName("capacity") val Capacity: String,
    @SerializedName("is_busy") val Is_Busy: Boolean,
    @SerializedName("user") val User: String,
    @SerializedName("date") val Date: String?
)
