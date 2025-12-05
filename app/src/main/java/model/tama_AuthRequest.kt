package model

import com.google.gson.annotations.SerializedName

data class tama_AuthRequest(

    @SerializedName("username")
    val tama_username: String,

    @SerializedName("password")
    val tama_password: String
)