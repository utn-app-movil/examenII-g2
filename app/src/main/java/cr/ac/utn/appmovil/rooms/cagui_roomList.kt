package cr.ac.utn.appmovil.rooms

import Service.cagui_APIService
import adapter.cagui_RoomAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.cagui_Room
import model.cagui_RoomBookingRequest
import util.util

class cagui_roomList : AppCompatActivity() {

    private lateinit var rvRooms: RecyclerView
    private lateinit var btnRefresh: Button
    private lateinit var btnCreateNewRoom: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var roomAdapter: cagui_RoomAdapter
    private var roomList: List<cagui_Room> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cagui_activity_room_list)

        initializeViews()
        setupRecyclerView()
        setupListeners()
        loadRooms()
    }

    override fun onResume() {
        super.onResume()
        loadRooms()
    }

    private fun initializeViews() {
        rvRooms = findViewById(R.id.cagui_rvRooms)
        btnRefresh = findViewById(R.id.cagui_btnRefresh)
        btnCreateNewRoom = findViewById(R.id.cagui_btnCreateNewRoom)
        progressBar = findViewById(R.id.cagui_progressBarList)
    }

    private fun setupRecyclerView() {
        roomAdapter = cagui_RoomAdapter(roomList) { room ->
            onRoomClicked(room)
        }
        rvRooms.layoutManager = LinearLayoutManager(this)
        rvRooms.adapter = roomAdapter
    }

    private fun setupListeners() {
        btnRefresh.setOnClickListener {
            loadRooms()
        }

        btnCreateNewRoom.setOnClickListener {
            util.openActivity(this, cagui_room::class.java)
        }
    }

    private fun loadRooms() {
        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = cagui_APIService.api.getRooms()

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    handleRoomsResponse(response.responseCode, response.message, response.data)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@cagui_roomList,
                        getString(R.string.cagui_error_network),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleRoomsResponse(responseCode: String, message: String, data: List<cagui_Room>?) {
        when (responseCode) {
            "SUCESSFUL", "INFO_FOUND" -> {
                if (data != null) {
                    roomList = data
                    roomAdapter.updateRooms(roomList)
                } else {
                    Toast.makeText(this, "No rooms available", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onRoomClicked(room: cagui_Room) {
        if (room.isBusy) {
            showReleaseDialog(room)
        } else {
            showBookingDialog(room)
        }
    }

    private fun showBookingDialog(room: cagui_Room) {
        util.showDialogCondition(
            this,
            getString(R.string.cagui_confirm_booking),
            getString(R.string.cagui_booking_question) + " ${room.room}?",
            getString(R.string.cagui_yes),
            getString(R.string.cagui_no),
            { bookRoom(room.room) },
            {}
        )
    }

    private fun showReleaseDialog(room: cagui_Room) {
        util.showDialogCondition(
            this,
            getString(R.string.cagui_confirm_release),
            getString(R.string.cagui_release_question) + " ${room.room}?",
            getString(R.string.cagui_yes),
            getString(R.string.cagui_no),
            { releaseRoom(room.room) },
            {}
        )
    }

    private fun bookRoom(roomName: String) {
        showLoading(true)

        val sharedPref = getSharedPreferences("cagui_prefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""
        val email = sharedPref.getString("email", "") ?: ""

        val userToSend = if (email.isNotEmpty()) email else username

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = cagui_RoomBookingRequest(roomName, userToSend)
                val response = cagui_APIService.api.bookRoom(request)

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    handleBookingResponse(response.responseCode, response.message)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@cagui_roomList,
                        getString(R.string.cagui_error_network),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun releaseRoom(roomName: String) {
        showLoading(true)

        val sharedPref = getSharedPreferences("cagui_prefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""
        val email = sharedPref.getString("email", "") ?: ""

        val userToSend = if (email.isNotEmpty()) email else username

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = cagui_RoomBookingRequest(roomName, userToSend)
                val response = cagui_APIService.api.unbookRoom(request)

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    handleUnbookingResponse(response.responseCode, response.message)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@cagui_roomList,
                        getString(R.string.cagui_error_network),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleBookingResponse(responseCode: String, message: String) {
        when (responseCode) {
            "SUCESSFUL", "INFO_UPDATED" -> {
                Toast.makeText(this, getString(R.string.cagui_room_booked_success), Toast.LENGTH_SHORT).show()
                loadRooms()
            }
            "INFO_ALREADY_EXISTS" -> {
                Toast.makeText(this, getString(R.string.cagui_room_already_booked), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleUnbookingResponse(responseCode: String, message: String) {
        when (responseCode) {
            "SUCESSFUL", "INFO_UPDATED" -> {
                Toast.makeText(this, getString(R.string.cagui_room_released_success), Toast.LENGTH_SHORT).show()
                loadRooms()
            }
            else -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnRefresh.isEnabled = !isLoading
        btnCreateNewRoom.isEnabled = !isLoading
    }
}
