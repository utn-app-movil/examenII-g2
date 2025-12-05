package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.adapter.kris_RoomAdapter
import cr.ac.utn.appmovil.rooms.kris_repository.kris_RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activity_kris_room_list : AppCompatActivity() {

    private lateinit var kris_recyclerRooms: RecyclerView
    private lateinit var kris_btnRefresh: Button
    private lateinit var kris_adapter: kris_RoomAdapter

    private val kris_roomRepository = kris_RoomRepository()
    private var kris_username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kris_room_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kris_room_list_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val kris_btnGoCreateRoom: Button = findViewById(R.id.kris_btnGoCreateRoom)
        kris_btnGoCreateRoom.setOnClickListener {
            startActivity(Intent(this, kris_room_activity::class.java))
        }

        val prefs = getSharedPreferences("kris_prefs", MODE_PRIVATE)
        kris_username = prefs.getString("kris_username", "") ?: ""

        kris_recyclerRooms = findViewById(R.id.kris_recyclerRooms)
        kris_btnRefresh = findViewById(R.id.kris_btnRefreshRooms)


        kris_adapter = kris_RoomAdapter(emptyList()) { room ->
            if (room.is_busy) {
                kris_unbookRoom(room.room)
            } else {
                kris_bookRoom(room.room)
            }
        }

        kris_recyclerRooms.layoutManager = LinearLayoutManager(this)
        kris_recyclerRooms.adapter = kris_adapter

        kris_btnRefresh.setOnClickListener {
            kris_loadRooms()
        }


        kris_loadRooms()
    }

    private fun kris_loadRooms() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = kris_roomRepository.getRooms()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.responseCode == "SUCESSFUL") {
                            kris_adapter.updateData(body.data ?: emptyList())
                        } else {
                            Toast.makeText(this@activity_kris_room_list, body?.message ?: "Error loading rooms", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@activity_kris_room_list, "HTTP Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_kris_room_list, "Exception: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun kris_bookRoom(roomName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = kris_roomRepository.bookRoom(roomName, kris_username)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_kris_room_list, response.body()?.message ?: "Error booking room", Toast.LENGTH_LONG).show()
                    kris_loadRooms()
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_kris_room_list, "Exception: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun kris_unbookRoom(roomName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = kris_roomRepository.unbookRoom(roomName)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_kris_room_list, response.body()?.message ?: "Error releasing room", Toast.LENGTH_LONG).show()
                    kris_loadRooms()
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_kris_room_list, "Exception: ${ex.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
