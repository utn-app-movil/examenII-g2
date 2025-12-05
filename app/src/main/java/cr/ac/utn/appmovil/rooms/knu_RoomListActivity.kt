package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import Service.knu_APIService
import adapter.knu_RoomAdapter
import cr.ac.utn.appmovil.rooms.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class knu_RoomListActivity : AppCompatActivity() {

    private lateinit var txtUserHeader: TextView
    private lateinit var recyclerRooms: RecyclerView
    private lateinit var adapter: knu_RoomAdapter
    private lateinit var btnRefresh: Button

    private lateinit var txtNewRoomName: EditText
    private lateinit var txtNewRoomCapacity: EditText
    private lateinit var btnCreateRoom: Button

    private var knu_username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knu_room_list)

        // Recibir el correo/usuario desde el login
        knu_username = intent.getStringExtra("knu_username") ?: ""

        txtUserHeader = findViewById(R.id.knu_txtUserHeader)
        txtUserHeader.text =
            if (knu_username.isNotEmpty()) "User: $knu_username" else "User: (not logged)"

        recyclerRooms = findViewById(R.id.knu_recyclerRooms)
        recyclerRooms.layoutManager = LinearLayoutManager(this)

        // Al tocar una sala, ir al detalle enviando username + room
        adapter = knu_RoomAdapter(mutableListOf()) { room ->
            val intent = Intent(this, knu_RoomActivity::class.java)
            intent.putExtra("knu_room_name", room.room)
            intent.putExtra("knu_username", knu_username)
            startActivity(intent)
        }
        recyclerRooms.adapter = adapter

        btnRefresh = findViewById(R.id.knu_btnRefreshRooms)
        btnRefresh.setOnClickListener { knu_loadRooms() }

        txtNewRoomName = findViewById(R.id.knu_txtNewRoomName)
        txtNewRoomCapacity = findViewById(R.id.knu_txtNewRoomCapacity)
        btnCreateRoom = findViewById(R.id.knu_btnCreateRoom)

        btnCreateRoom.setOnClickListener { knu_createRoom() }

        // Carga inicial
        knu_loadRooms()
    }

    private fun knu_loadRooms() {
        knu_APIService.api.knu_getRooms()
            .enqueue(object : Callback<knu_RoomListResponse> {
                override fun onResponse(
                    call: Call<knu_RoomListResponse>,
                    response: Response<knu_RoomListResponse>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@knu_RoomListActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val body = response.body()
                    if (body?.data != null) {
                        adapter.knu_setData(body.data)
                    } else {
                        Toast.makeText(
                            this@knu_RoomListActivity,
                            body?.message ?: "Empty list",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<knu_RoomListResponse>, t: Throwable) {
                    Toast.makeText(
                        this@knu_RoomListActivity,
                        "Failure: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun knu_createRoom() {
        val name = txtNewRoomName.text.toString().trim()
        val capacityStr = txtNewRoomCapacity.text.toString().trim()

        if (name.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, "Room and capacity are required", Toast.LENGTH_SHORT).show()
            return
        }

        val capacity = capacityStr.toIntOrNull()
        if (capacity == null || capacity <= 0) {
            Toast.makeText(this, "Capacity must be a positive number", Toast.LENGTH_SHORT).show()
            return
        }

        val request = knu_CreateRoomRequest(name, capacity)

        knu_APIService.api.knu_createRoom(request)
            .enqueue(object : Callback<knu_RoomResponse> {
                override fun onResponse(
                    call: Call<knu_RoomResponse>,
                    response: Response<knu_RoomResponse>
                ) {
                    val body = response.body()
                    Toast.makeText(
                        this@knu_RoomListActivity,
                        body?.message ?: "Room creation response",
                        Toast.LENGTH_SHORT
                    ).show()
                    knu_loadRooms()
                }

                override fun onFailure(call: Call<knu_RoomResponse>, t: Throwable) {
                    Toast.makeText(
                        this@knu_RoomListActivity,
                        "Failure: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
