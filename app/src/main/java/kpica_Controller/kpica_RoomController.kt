package kpica_Controller

import Service.kpica_APIService
import android.content.Context
import android.util.Log
import cr.ac.utn.appmovil.rooms.R
import kpica_Entity.kpica_DTORoom
import kpica_Entity.kpica_Room
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class kpica_RoomController {
    private  var context: Context

    constructor(context: Context){
        this.context=context
    }

    suspend fun getRoom(): List<kpica_Room>{
        var rooms = mutableListOf<kpica_Room>()
        try {
            val response = kpica_APIService.apiRoom.getAll()
            if (response.responseCode != context.getString(R.string.kpica_ResponseSuccess))
                throw Exception(response.message)

            response.data.forEach { item ->
                rooms.add(getRoomObject(item))
            }
            Log.d("API_Call", "Success: ${response.data}")
        } catch (e: Exception) {
            // Handle error
            Log.e("API_Call", "Error fetching data: ${e.message}")
            throw Exception(e.message)
        }
        return rooms
    }

    private fun getRoomObject (item: kpica_DTORoom): kpica_Room {
        val room = kpica_Room()
        room.Room = item.Room
        room.Capacity = item.Capacity.toInt()
        room.Is_Busy = item.Is_Busy
        room.User = item.User
        val bDateParse = parseStringToDateModern(item.Date.toString(),
            "yyyy-MM-dd")
        room.Date = LocalDate.of(bDateParse?.year!!, bDateParse.month.value,
            bDateParse?.dayOfMonth!!)

        return room
    }

    private fun parseStringToDateModern(dateString: String, pattern: String): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            LocalDate.parse(dateString, formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}