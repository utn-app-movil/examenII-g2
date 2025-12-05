package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import cr.ac.utn.appmovil.rooms.adapter.jor_RoomAdapter
import cr.ac.utn.appmovil.rooms.identities.jor_Room
import cr.ac.utn.appmovil.rooms.service.jor_APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.util

class jor_RoomListActivity : AppCompatActivity() {

    private lateinit var jor_rv_rooms: RecyclerView
    private lateinit var jor_fab_add: FloatingActionButton
    private lateinit var jor_btn_refresh: Button
    private lateinit var adapter: jor_RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jor_room_list)

        jor_rv_rooms = findViewById(R.id.jor_rv_rooms)
        jor_fab_add = findViewById(R.id.jor_fab_add)
        jor_btn_refresh = findViewById(R.id.jor_btn_refresh)

        setupRecyclerView()
        loadRooms()

        jor_fab_add.setOnClickListener {
            util.openActivity(this, jor_RoomActivity::class.java)
        }

        jor_btn_refresh.setOnClickListener {
            loadRooms()
        }
    }

    private fun setupRecyclerView() {
        adapter = jor_RoomAdapter(mutableListOf()) { room ->
            if (room.is_busy) {
                unbookRoom(room.room)
            } else {
                bookRoom(room.room)
            }
        }
        jor_rv_rooms.layoutManager = LinearLayoutManager(this)
        jor_rv_rooms.adapter = adapter
    }

    private fun loadRooms() {
        jor_APIService.instance.getRooms().enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@jor_RoomListActivity, "Error del servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                val body = response.body() ?: return

                val roomsList = mutableListOf<jor_Room>()

                when (body) {
                    // Caso 1: Viene directo la lista (como en tu Postman)
                    is List<*> -> {
                        (body as List<Map<String, Any>>).forEach { map ->
                            try {
                                roomsList.add(
                                    jor_Room(
                                        room = map["room"] as String,
                                        capacity = (map["capacity"] as Number).toInt(),
                                        is_busy = map["is_busy"] as Boolean,
                                        user = map["user"] as? String ?: ""
                                    )
                                )
                            } catch (e: Exception) { }
                        }
                    }
                    // Caso 2: Viene envuelto en "data"
                    is Map<*, *> -> {
                        val data = (body as Map<String, Any>)["data"]
                        if (data is List<*>) {
                            (data as List<Map<String, Any>>).forEach { map ->
                                try {
                                    roomsList.add(
                                        jor_Room(
                                            room = map["room"] as String,
                                            capacity = (map["capacity"] as Number).toInt(),
                                            is_busy = map["is_busy"] as Boolean,
                                            user = map["user"] as? String ?: ""
                                        )
                                    )
                                } catch (e: Exception) { }
                            }
                        }
                    }
                }

                adapter.updateRooms(roomsList)

                if (roomsList.isEmpty()) {
                    Toast.makeText(this@jor_RoomListActivity, "No hay salas disponibles", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@jor_RoomListActivity, "Sin conexión a internet", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun bookRoom(roomName: String) {
        val body = mapOf(
            "room" to roomName,
            "username" to jor_AuthenticationActivity.CURRENT_USER_EMAIL
        )

        jor_APIService.instance.bookRoom(body).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                loadRooms()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {}
        })
    }

    private fun unbookRoom(roomName: String) {
        val body = mapOf("room" to roomName)

        jor_APIService.instance.unbookRoom(body).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                loadRooms()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadRooms()
    }
}