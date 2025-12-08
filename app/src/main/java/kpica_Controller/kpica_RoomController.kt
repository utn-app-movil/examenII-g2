package kpica_Controller

import Service.kpica_APIService
import android.content.Context
import android.util.Log
import android.widget.Toast
import cr.ac.utn.appmovil.rooms.R
import kpica_Entity.kpica_DTORoom
import kpica_Entity.kpica_DTORoom_Booking
import kpica_Entity.kpica_DTORoom_Insert
import kpica_Entity.kpica_DTORoom_UnBooking
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

    suspend fun createRoom(room: kpica_Room) {
        try {
            val response = kpica_APIService.apiRoom.createRoom(convertToDTORoomInsertObject(room))
            if (response.responseCode == context.getString(R.string.kpica_ResponseSuccess)){
                Log.d("API_Call", "Success: ${response.data}")
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    suspend fun updateBookingRoom(room: kpica_Room) {
        try {
            val response = kpica_APIService.apiRoom.updateBookingRoom(convertToDTORoomBookingObject(room))
            if (response.responseCode == context.getString(R.string.kpica_ResponseSuccess)) {
                Log.d("API_Call", "Success: ${response.data}")
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                throw Exception(response.message)
            }
        } catch (e: Exception) {
                throw Exception(e.message)
        }
    }

    suspend fun updateUnBookingRoom(room: kpica_Room) {
        try {
            val response = kpica_APIService.apiRoom.updateUnBookingRoom(convertToDTORoomUnBookingObject(room))
            if (response.responseCode == context.getString(R.string.kpica_ResponseSuccess)) {
                Log.d("API_Call", "Success: ${response.data}")
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                throw Exception(response.message)
            }
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    private fun getRoomObject (item: kpica_DTORoom): kpica_Room {
        val room = kpica_Room()
        room.Room = item.Room
        room.Capacity = item.Capacity.toInt()
        room.Is_Busy = item.Is_Busy
        room.User = item.User
        if (item.Date != null) {
            val bDateParse = parseStringToDateModern(item.Date,
                "yyyy-MM-d")
            room.Date = LocalDate.of(bDateParse?.year!!, bDateParse.month.value,
                bDateParse?.dayOfMonth!!)
        }

        return room
    }

    private fun convertToDTORoomInsertObject (room: kpica_Room): kpica_DTORoom_Insert {
        return kpica_DTORoom_Insert(room.Room, room.Capacity.toString())
    }

    private fun convertToDTORoomBookingObject (room: kpica_Room): kpica_DTORoom_Booking {
        return kpica_DTORoom_Booking(room.Room, room.User)
    }

    private fun convertToDTORoomUnBookingObject (room: kpica_Room): kpica_DTORoom_UnBooking {
        return kpica_DTORoom_UnBooking(room.Room)
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