package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import Service.knu_APIService
import cr.ac.utn.appmovil.rooms.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class knu_RoomActivity : AppCompatActivity() {

    private lateinit var txtRoomName: TextView
    private lateinit var txtRoomCapacity: TextView
    private lateinit var txtRoomStatus: TextView
    private lateinit var txtRoomDate: TextView
    private lateinit var txtRoomUser: TextView

    private lateinit var btnBook: Button
    private lateinit var btnUnbook: Button

    private var knu_username: String = ""
    private var knu_roomName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knu_room)

        // Recibir usuario y sala desde la lista
        knu_username = intent.getStringExtra("knu_username") ?: ""
        knu_roomName = intent.getStringExtra("knu_room_name") ?: ""

        txtRoomName = findViewById(R.id.knu_txtDetailRoomName)
        txtRoomCapacity = findViewById(R.id.knu_txtDetailRoomCapacity)
        txtRoomStatus = findViewById(R.id.knu_txtDetailRoomStatus)
        txtRoomDate = findViewById(R.id.knu_txtDetailRoomDate)
        txtRoomUser = findViewById(R.id.knu_txtDetailRoomUser)

        btnBook = findViewById(R.id.knu_btnBookRoom)
        btnUnbook = findViewById(R.id.knu_btnUnbookRoom)

        // Debug visual: ver con qué usuario llegamos
        if (knu_username.isNotEmpty()) {
            Toast.makeText(this, "Booking as: $knu_username", Toast.LENGTH_SHORT).show()
        }

        btnBook.setOnClickListener { knu_bookRoom() }
        btnUnbook.setOnClickListener { knu_unbookRoom() }

        if (knu_roomName.isNotEmpty()) {
            knu_loadRoom()
        } else {
            Toast.makeText(this, "Room name not provided", Toast.LENGTH_SHORT).show()
        }
    }

    private fun knu_loadRoom() {
        knu_APIService.api.knu_getRoom(knu_roomName)
            .enqueue(object : Callback<knu_RoomResponse> {
                override fun onResponse(
                    call: Call<knu_RoomResponse>,
                    response: Response<knu_RoomResponse>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@knu_RoomActivity,
                            "Error loading room: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val body = response.body()
                    val room = body?.data

                    if (room != null) {
                        txtRoomName.text = room.room
                        txtRoomCapacity.text = "Capacity: ${room.capacity}"
                        txtRoomStatus.text = if (room.is_busy) "Status: Busy" else "Status: Available"
                        txtRoomDate.text = "Date: ${room.date ?: "-"}"
                        txtRoomUser.text = "User: ${room.user ?: "-"}"
                    } else {
                        Toast.makeText(
                            this@knu_RoomActivity,
                            body?.message ?: "Room not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<knu_RoomResponse>, t: Throwable) {
                    Toast.makeText(
                        this@knu_RoomActivity,
                        "Failure: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun knu_bookRoom() {
        if (knu_username.isEmpty()) {
            Toast.makeText(
                this,
                "Login required to book a room",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val request = knu_BookRoomRequest(
            username = knu_username,
            room = knu_roomName
        )

        knu_APIService.api.knu_bookRoom(request)
            .enqueue(object : Callback<knu_RoomResponse> {
                override fun onResponse(
                    call: Call<knu_RoomResponse>,
                    response: Response<knu_RoomResponse>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@knu_RoomActivity,
                            "Error booking room: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val body = response.body()
                    Toast.makeText(
                        this@knu_RoomActivity,
                        body?.message ?: "Booking response",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Refrescar datos de la sala
                    knu_loadRoom()
                }

                override fun onFailure(call: Call<knu_RoomResponse>, t: Throwable) {
                    Toast.makeText(
                        this@knu_RoomActivity,
                        "Failure: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun knu_unbookRoom() {
        val request = knu_UnbookRoomRequest(knu_roomName)

        knu_APIService.api.knu_unbookRoom(request)
            .enqueue(object : Callback<knu_RoomResponse> {
                override fun onResponse(
                    call: Call<knu_RoomResponse>,
                    response: Response<knu_RoomResponse>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@knu_RoomActivity,
                            "Error unbooking room: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val body = response.body()
                    Toast.makeText(
                        this@knu_RoomActivity,
                        body?.message ?: "Unbooking response",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Refrescar datos de la sala
                    knu_loadRoom()
                }

                override fun onFailure(call: Call<knu_RoomResponse>, t: Throwable) {
                    Toast.makeText(
                        this@knu_RoomActivity,
                        "Failure: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
