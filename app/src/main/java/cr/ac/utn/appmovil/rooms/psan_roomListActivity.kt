package cr.ac.utn.appmovil.rooms

import Service.psan_APIService
import adapter.psan_roomAdapter
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import model.*

class psan_roomListActivity : AppCompatActivity() {

    private lateinit var listRooms: RecyclerView
    private lateinit var btnRefresh: Button
    private lateinit var btnCreateRoom: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psan_room_list)

        listRooms = findViewById(R.id.listRooms)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnCreateRoom = findViewById(R.id.btnCreateRoom)

        btnRefresh.setOnClickListener { loadRooms() }
        btnCreateRoom.setOnClickListener {
            util.util.openActivity(this, psan_roomCreateActivity::class.java)
        }

        loadRooms()
    }

    private fun loadRooms() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = psan_APIService.api.psan_getRooms()

            runOnUiThread {
                val rooms = response.body()?.data ?: emptyList()
                listRooms.layoutManager = LinearLayoutManager(this@psan_roomListActivity)
                listRooms.adapter = psan_roomAdapter(rooms) { room ->
                    if (room.is_busy) unbookRoom(room.room)
                    else bookRoom(room.room)
                }
            }
        }
    }

    private fun bookRoom(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = psan_APIService.api.psan_bookRoom(
                psan_roomBookingRequest(name, "psanchez@est.utn.ac.cr")
            )

            runOnUiThread {
                Toast.makeText(this@psan_roomListActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                loadRooms()
            }
        }
    }

    private fun unbookRoom(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = psan_APIService.api.psan_unbookRoom(
                psan_roomBookingRequest(name)
            )

            runOnUiThread {
                Toast.makeText(this@psan_roomListActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                loadRooms()
            }
        }
    }
}