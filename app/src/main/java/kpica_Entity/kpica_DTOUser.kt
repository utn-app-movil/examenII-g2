package kpica_Entity

import com.google.gson.annotations.SerializedName

data class kpica_DTOUser (
    @SerializedName("username") val User: String,
    @SerializedName("password") val Password: String?,
    @SerializedName("name") val Name: String,
    @SerializedName("isActive") val IsActive: Boolean?,
    @SerializedName("lastname") val LastName: String,
    @SerializedName("email") val Email: String,
)