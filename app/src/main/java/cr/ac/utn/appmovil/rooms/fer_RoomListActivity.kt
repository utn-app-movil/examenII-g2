package cr.ac.utn.appmovil.rooms

import Service.fer_APIService
import adapter.fer_RoomAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import model.fer_BookingRequest
import model.fer_Room
import model.fer_UnbookingRequest
import util.util

class fer_RoomListActivity : AppCompatActivity() {

    private lateinit var fer_recyclerView: RecyclerView
    private lateinit var fer_adapter: fer_RoomAdapter
    private lateinit var fer_btnRefresh: Button
    private lateinit var fer_btnCreateRoom: Button
    private var fer_username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fer_activity_room_list)

        fer_username = intent.getStringExtra("username") ?: ""

        fer_recyclerView = findViewById(R.id.fer_recycler_rooms)
        fer_btnRefresh = findViewById(R.id.fer_btn_refresh)
        fer_btnCreateRoom = findViewById(R.id.fer_btn_create_room)

        fer_recyclerView.layoutManager = LinearLayoutManager(this)
        fer_adapter = fer_RoomAdapter(emptyList()) { room ->
            fer_handleRoomClick(room)
        }
        fer_recyclerView.adapter = fer_adapter

        fer_btnRefresh.setOnClickListener {
            fer_loadRooms()
        }

        fer_btnCreateRoom.setOnClickListener {
            util.openActivity(this, fer_RoomActivity::class.java)
        }

        fer_loadRooms()
    }

    override fun onResume() {
        super.onResume()
        fer_loadRooms()
    }

    private fun fer_loadRooms() {
        lifecycleScope.launch {
            try {
                val response = fer_APIService.apiService.getRooms()

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null && apiResponse.responseCode == "SUCESSFUL") {
                        val rooms = apiResponse.data ?: emptyList()
                        fer_adapter.fer_updateRooms(rooms)
                    } else {
                        Toast.makeText(
                            this@fer_RoomListActivity,
                            apiResponse?.message ?: getString(R.string.fer_error_loading_rooms),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@fer_RoomListActivity,
                        getString(R.string.fer_error_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@fer_RoomListActivity,
                    "${getString(R.string.fer_error_exception)}: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun fer_handleRoomClick(room: fer_Room) {
        if (room.is_busy) {
            // Room is occupied, ask to release it
            util.showDialogCondition(
                this,
                getString(R.string.fer_release_room_title),
                getString(R.string.fer_release_room_message, room.room),
                getString(R.string.fer_yes),
                getString(R.string.fer_no),
                { fer_unbookRoom(room) },
                { }
            )
        } else {
            // Room is available, ask to book it
            util.showDialogCondition(
                this,
                getString(R.string.fer_book_room_title),
                getString(R.string.fer_book_room_message, room.room),
                getString(R.string.fer_yes),
                getString(R.string.fer_no),
                { fer_bookRoom(room) },
                { }
            )
        }
    }

    private fun fer_bookRoom(room: fer_Room) {
        lifecycleScope.launch {
            try {
                val bookingRequest = fer_BookingRequest(room.room, fer_username)
                val response = fer_APIService.apiService.bookRoom(bookingRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null) {
                        Toast.makeText(
                            this@fer_RoomListActivity,
                            apiResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (apiResponse.responseCode == "SUCESSFUL") {
                            fer_loadRooms()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@fer_RoomListActivity,
                        getString(R.string.fer_error_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@fer_RoomListActivity,
                    "${getString(R.string.fer_error_exception)}: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun fer_unbookRoom(room: fer_Room) {
        lifecycleScope.launch {
            try {
                val unbookingRequest = fer_UnbookingRequest(room.room)
                val response = fer_APIService.apiService.unbookRoom(unbookingRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null) {
                        Toast.makeText(
                            this@fer_RoomListActivity,
                            apiResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (apiResponse.responseCode == "SUCESSFUL") {
                            fer_loadRooms()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@fer_RoomListActivity,
                        getString(R.string.fer_error_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@fer_RoomListActivity,
                    "${getString(R.string.fer_error_exception)}: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
