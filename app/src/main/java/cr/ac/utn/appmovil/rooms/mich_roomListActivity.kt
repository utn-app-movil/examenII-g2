package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.mich_RoomAdapter
import model.mich_Room
import model.mich_RoomListResponse
import Service.mich_APIService
import model.mich_RoomBookingRequest
import model.mich_RoomBookingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class mich_roomListActivity : AppCompatActivity() {

    private lateinit var rvRooms: RecyclerView
    private lateinit var btnRefresh: Button
    private var username: String = "estudiante"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mich_room_list)

        username = intent.getStringExtra("username") ?: "estudiante"

        rvRooms = findViewById(R.id.rvRooms)
        btnRefresh = findViewById(R.id.btnRefresh)

        rvRooms.layoutManager = LinearLayoutManager(this)

        btnRefresh.setOnClickListener {
            loadRooms()
        }

        loadRooms()
    }

    private fun loadRooms() {

        mich_APIService.api.mich_getRooms()
            .enqueue(object : Callback<mich_RoomListResponse> {
                override fun onResponse(
                    call: Call<mich_RoomListResponse>,
                    response: Response<mich_RoomListResponse>
                ) {
                    val body = response.body()

                    if (response.isSuccessful && body?.data != null) {

                        val rooms: List<mich_Room> = body.data!!

                        rvRooms.adapter = mich_RoomAdapter(rooms) { room ->
                            handleRoomAction(room)
                        }

                    } else {
                        Toast.makeText(
                            this@mich_roomListActivity,
                            body?.message ?: "Error al obtener salas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<mich_RoomListResponse>, t: Throwable) {
                    Toast.makeText(
                        this@mich_roomListActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun handleRoomAction(room: mich_Room) {
        if (room.is_busy) {
            liberateRoom(room.room)
        } else {
            reserveRoom(room.room)
        }
    }

    private fun reserveRoom(roomName: String) {
        val request = mich_RoomBookingRequest(
            room = roomName,
            username = username
        )

        mich_APIService.api.mich_booking(request)
            .enqueue(object : Callback<mich_RoomBookingResponse> {
                override fun onResponse(
                    call: Call<mich_RoomBookingResponse>,
                    response: Response<mich_RoomBookingResponse>
                ) {
                    val body = response.body()

                    if (response.isSuccessful && body != null) {

                        if (body.responseCode == "SUCESSFUL") {
                            Toast.makeText(
                                this@mich_roomListActivity,
                                "Sala reservada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadRooms()
                        } else {
                            Toast.makeText(
                                this@mich_roomListActivity,
                                body.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this@mich_roomListActivity,
                            "Error al reservar sala",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<mich_RoomBookingResponse>, t: Throwable) {
                    Toast.makeText(
                        this@mich_roomListActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun liberateRoom(roomName: String) {

        val request = mich_RoomBookingRequest(
            room = roomName,
            username = null
        )

        mich_APIService.api.mich_unbooking(request)
            .enqueue(object : Callback<mich_RoomBookingResponse> {
                override fun onResponse(
                    call: Call<mich_RoomBookingResponse>,
                    response: Response<mich_RoomBookingResponse>
                ) {
                    val body = response.body()

                    if (response.isSuccessful && body != null) {

                        if (body.responseCode == "SUCESSFUL") {
                            Toast.makeText(
                                this@mich_roomListActivity,
                                "Sala liberada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadRooms()
                        } else {
                            Toast.makeText(
                                this@mich_roomListActivity,
                                body.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this@mich_roomListActivity,
                            "Error al liberar sala",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<mich_RoomBookingResponse>, t: Throwable) {
                    Toast.makeText(
                        this@mich_roomListActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
