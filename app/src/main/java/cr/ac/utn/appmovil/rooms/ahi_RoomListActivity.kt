package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.ahi_Room
import Service.ahi_RoomRepository
import adapter.ahi_RoomAdapter
import util.EXTRA_ID
import util.util

class ahi_RoomListActivity : AppCompatActivity() {

    private val repository = ahi_RoomRepository()
    private lateinit var adapter: ahi_RoomAdapter
    private var roomList = mutableListOf<ahi_Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahi_room_list)

        val rv = findViewById<RecyclerView>(R.id.rv_ahi_rooms)
        val btnRefresh = findViewById<Button>(R.id.btn_ahi_refresh)
        val btnCreate = findViewById<Button>(R.id.btn_ahi_create_room)

        adapter = ahi_RoomAdapter(roomList) { room ->
            if (room.is_busy) {
                util.showDialogCondition(
                    this,
                    "Liberar sala",
                    "¿Desea liberar la sala ${room.room}?",
                    "Sí", "No",
                    { liberateRoom(room.room) },
                    { }
                )
            } else {
                util.showDialogCondition(
                    this,
                    "Reservar sala",
                    "¿Desea reservar la sala ${room.room}?",
                    "Sí", "No",
                    { reserveRoom(room.room) },
                    { }
                )
            }
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        btnRefresh.setOnClickListener { loadRooms() }
        btnCreate.setOnClickListener { util.openActivity(this, ahi_CreateRoomActivity::class.java) }

        loadRooms()
    }

    private fun loadRooms() {
        lifecycleScope.launch {
            val (list, msg) = repository.getRooms()
            if (list != null) {
                roomList.clear()
                roomList.addAll(list)
                adapter.notifyDataSetChanged()
            }
            Toast.makeText(this@ahi_RoomListActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun reserveRoom(roomName: String) {
        lifecycleScope.launch {
            val userEmail = repository.currentUserEmail
            if (userEmail.isNullOrEmpty()) {
                Toast.makeText(this@ahi_RoomListActivity, "Error: no se pudo obtener el usuario", Toast.LENGTH_LONG).show()
                return@launch
            }
            println("RESERVANDO SALA: $roomName con usuario: $userEmail")
            val msg = repository.bookRoom(roomName, userEmail)
            Toast.makeText(this@ahi_RoomListActivity, msg, Toast.LENGTH_LONG).show()
            loadRooms()
        }
    }

    private fun liberateRoom(roomName: String) {
        lifecycleScope.launch {
            val msg = repository.unbookRoom(roomName)
            Toast.makeText(this@ahi_RoomListActivity, msg, Toast.LENGTH_LONG).show()
            loadRooms()
        }
    }
}