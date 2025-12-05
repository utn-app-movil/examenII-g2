package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import sebas_controller.RoomController

class SebasRoomListActivity : AppCompatActivity() {
    
    private lateinit var rvRoomList: RecyclerView
    private lateinit var btnRefresh: Button
    private lateinit var btnCreateNewRoom: Button
    private lateinit var tvEmptyState: TextView
    private lateinit var pbRoomList: ProgressBar
    private lateinit var adapter: SebasRoomAdapter
    private lateinit var roomController: RoomController
    private var username: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sebas_activity_room_list)
        
        username = intent.getStringExtra("username") ?: "" //nos llevamos el username
        roomController = RoomController(this)
        
        rvRoomList = findViewById(R.id.sebas_rv_room_list)
        btnRefresh = findViewById(R.id.sebas_btn_refresh)
        btnCreateNewRoom = findViewById(R.id.sebas_btn_create_new_room)
        tvEmptyState = findViewById(R.id.sebas_tv_empty_state)
        pbRoomList = findViewById(R.id.sebas_pb_room_list)
        
        adapter = SebasRoomAdapter(emptyList()) { room ->
            if (room.isBusy) {
                unbookRoom(room)
            } else {
                bookRoom(room)
            }
        }
        
        rvRoomList.layoutManager = LinearLayoutManager(this)
        rvRoomList.adapter = adapter
        
        btnRefresh.setOnClickListener {
            loadRooms()
        }
        
        btnCreateNewRoom.setOnClickListener {
            val intent = Intent(this, SebasRoomActivity::class.java)
            startActivity(intent)
        }
        
        loadRooms()
    }
    
    override fun onResume() {
        super.onResume()
        loadRooms()
    }
    
    private fun loadRooms() {
        pbRoomList.visibility = ProgressBar.VISIBLE
        tvEmptyState.visibility = TextView.GONE
        
        lifecycleScope.launch {
            try {
                val rooms = roomController.getRooms()
                
                pbRoomList.visibility = ProgressBar.GONE
                
                if (rooms.isEmpty()) {
                    tvEmptyState.visibility = TextView.VISIBLE
                } else {
                    adapter.updateRooms(rooms)
                }
            } catch (e: Exception) {
                pbRoomList.visibility = ProgressBar.GONE
                Toast.makeText(this@SebasRoomListActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun bookRoom(room: SebasRoom) {
        val input = android.widget.EditText(this)
        input.hint = "Enter email"
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Book Room: ${room.room}")
            .setMessage("Enter your email to book this room")
            .setView(input)
            .setPositiveButton("Book") { dialog, _ ->
                val email = input.text.toString()
                if (email.isEmpty()) {
                    Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        try {
                            roomController.bookRoom(room.room, email)
                            Toast.makeText(this@SebasRoomListActivity, getString(R.string.sebas_success_room_booked), Toast.LENGTH_SHORT).show()
                            loadRooms()
                        } catch (e: Exception) {
                            Toast.makeText(this@SebasRoomListActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    private fun unbookRoom(room: SebasRoom) {
        lifecycleScope.launch {
            try {
                roomController.unbookRoom(room.room)
                Toast.makeText(this@SebasRoomListActivity, getString(R.string.sebas_success_room_released), Toast.LENGTH_SHORT).show()
                loadRooms()
            } catch (e: Exception) {
                Toast.makeText(this@SebasRoomListActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
