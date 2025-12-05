package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.Service.carl_RetrofitClient
import cr.ac.utn.appmovil.rooms.adapter.carl_RoomAdapter
import cr.ac.utn.appmovil.rooms.model.carl_BookingRequest
import cr.ac.utn.appmovil.rooms.model.carl_Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class carl_RoomListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonRefresh: Button
    private lateinit var buttonCreateRoom: Button
    private lateinit var adapter: carl_RoomAdapter
    private var currentUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carl_activity_room_list)

        // Obtener usuario actual
        val sharedPref = getSharedPreferences("carl_prefs", MODE_PRIVATE)
        currentUser = sharedPref.getString("current_user", "") ?: ""

        recyclerView = findViewById(R.id.carl_recyclerViewRooms)
        buttonRefresh = findViewById(R.id.carl_buttonRefresh)
        buttonCreateRoom = findViewById(R.id.carl_buttonCreateRoom)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = carl_RoomAdapter(emptyList()) { room ->
            onRoomClick(room)
        }
        recyclerView.adapter = adapter

        buttonRefresh.setOnClickListener {
            loadRooms()
        }

        buttonCreateRoom.setOnClickListener {
            val intent = Intent(this, carl_CreateRoomActivity::class.java)
            startActivity(intent)
        }

        loadRooms()
    }

    override fun onResume() {
        super.onResume()
        loadRooms()
    }

    private fun loadRooms() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = carl_RetrofitClient.apiService.getRooms()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null && apiResponse.data != null) {
                            adapter.updateRooms(apiResponse.data)
                        }
                    } else {
                        Toast.makeText(this@carl_RoomListActivity, "Error loading rooms", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@carl_RoomListActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onRoomClick(room: carl_Room) {
        if (room.isBusy) {
            // Mostrar diálogo para liberar
            AlertDialog.Builder(this)
                .setTitle("Room Booked")
                .setMessage("This room is booked by ${room.user}. Do you want to unbook it?")
                .setPositiveButton("Unbook") { _, _ ->
                    unbookRoom(room)
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            // Mostrar diálogo para reservar
            AlertDialog.Builder(this)
                .setTitle("Book Room")
                .setMessage("Do you want to book ${room.room}?")
                .setPositiveButton("Book") { _, _ ->
                    bookRoom(room)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun bookRoom(room: carl_Room) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = carl_BookingRequest(
                    room = room.room,
                    username = currentUser
                )
                val response = carl_RetrofitClient.apiService.bookRoom(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Toast.makeText(
                            this@carl_RoomListActivity,
                            apiResponse?.message ?: "Room booked",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadRooms()
                    } else {
                        // Mostrar mensaje más descriptivo
                        Toast.makeText(
                            this@carl_RoomListActivity,
                            "Error ${response.code()}: This room might be already booked",
                            Toast.LENGTH_LONG
                        ).show()
                        loadRooms() // Refrescar para ver el estado real
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@carl_RoomListActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun unbookRoom(room: carl_Room) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = carl_BookingRequest(
                    room = room.room,
                    username = currentUser
                )
                val response = carl_RetrofitClient.apiService.unbookRoom(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Toast.makeText(
                            this@carl_RoomListActivity,
                            apiResponse?.message ?: "Room unbooked",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadRooms()
                    } else {
                        Toast.makeText(
                            this@carl_RoomListActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadRooms() // Refrescar para ver el estado real
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@carl_RoomListActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}