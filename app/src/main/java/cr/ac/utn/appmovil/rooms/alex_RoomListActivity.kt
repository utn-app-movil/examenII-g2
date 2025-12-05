package cr.ac.utn.appmovil.rooms

import adapter.alex_RoomAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class alex_RoomListActivity : AppCompatActivity() {

    private lateinit var adapter: alex_RoomAdapter
    private var currentUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alex_room_list)

        currentUser = intent.getStringExtra("CURRENT_USER") ?: ""

        val rv = findViewById<RecyclerView>(R.id.alex_rv_rooms)
        val btnRefresh = findViewById<Button>(R.id.alex_btn_refresh)
        val btnCreate = findViewById<Button>(R.id.alex_btn_go_create)
        val btnBack = findViewById<Button>(R.id.alex_btn_back_list)

        rv.layoutManager = LinearLayoutManager(this)

        adapter = alex_RoomAdapter(emptyList(), currentUser) { room, isBooking ->
            if (isBooking) {
                bookRoom(room.room)
            } else {
                unbookRoom(room.room)
            }
        }
        rv.adapter = adapter

        btnRefresh.setOnClickListener { loadRooms() }

        btnCreate.setOnClickListener {
            val intent = Intent(this, alex_CreateRoomActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }

        loadRooms()
    }

    override fun onResume() {
        super.onResume()
        loadRooms()
    }

    private fun loadRooms() {
        alex_APIService.api.getRooms().enqueue(object : Callback<alex_RoomResponse> {
            override fun onResponse(call: Call<alex_RoomResponse>, response: Response<alex_RoomResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!.data ?: emptyList()
                    adapter.updateList(list)
                }
            }

            override fun onFailure(call: Call<alex_RoomResponse>, t: Throwable) {
                Toast.makeText(this@alex_RoomListActivity, getString(R.string.alex_msg_load_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bookRoom(roomName: String) {
        val req = alex_BookingRequest(roomName, currentUser)

        alex_APIService.api.bookRoom(req).enqueue(object : Callback<alex_GenericResponse> {
            override fun onResponse(call: Call<alex_GenericResponse>, response: Response<alex_GenericResponse>) {
                if (response.body() != null) {
                    Toast.makeText(this@alex_RoomListActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    loadRooms()
                }
            }
            override fun onFailure(call: Call<alex_GenericResponse>, t: Throwable) {}
        })
    }

    private fun unbookRoom(roomName: String) {
        val req = alex_UnbookingRequest(roomName)

        alex_APIService.api.unbookRoom(req).enqueue(object : Callback<alex_GenericResponse> {
            override fun onResponse(call: Call<alex_GenericResponse>, response: Response<alex_GenericResponse>) {
                if (response.body() != null) {
                    Toast.makeText(this@alex_RoomListActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    loadRooms()
                }
            }
            override fun onFailure(call: Call<alex_GenericResponse>, t: Throwable) {}
        })
    }
}