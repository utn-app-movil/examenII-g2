package cr.ac.utn.appmovil.rooms

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

class   Walk_RoomListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var createButton: Button  // ⭐ Nuevo botón
    private lateinit var adapter: Walk_RoomAdapter
    private val roomList = mutableListOf<Walk_Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walk_activity_room_list)

        recycler = findViewById(R.id.walk_room_list_recycler)
        refreshButton = findViewById(R.id.walk_room_list_refresh)
        createButton = findViewById(R.id.walk_room_list_create) // ⭐ conectamos el botón

        adapter = Walk_RoomAdapter(
            rooms = roomList,
            onReserve = { room -> reserveRoom(room) },
            onUnreserve = { room -> unreserveRoom(room) }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        refreshButton.setOnClickListener { loadRooms() }

        // ⭐ Abrir pantalla para crear una sala
        createButton.setOnClickListener {
            startActivity(Intent(this, Walk_RoomActivity::class.java))
        }

        loadRooms()
    }

    private fun loadRooms() {
        Walk_ApiClient.roomService.getRooms()
            .enqueue(object : Callback<Walk_ApiResponse<List<Walk_Room>>> {
                override fun onResponse(
                    call: Call<Walk_ApiResponse<List<Walk_Room>>>,
                    response: Response<Walk_ApiResponse<List<Walk_Room>>>
                ) {
                    val body = response.body()
                    if (body?.data != null) {
                        roomList.clear()
                        roomList.addAll(body.data)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<Walk_ApiResponse<List<Walk_Room>>>, t: Throwable) {
                    Toast.makeText(
                        this@Walk_RoomListActivity,
                        "Error loading rooms",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun reserveRoom(room: Walk_Room) {

        // 🔥 Usamos EMAIL del usuario logueado
        val email = Walk_Session.currentUser?.email ?: run {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show()
            return
        }

        val req = mapOf(
            "room" to room.room,
            "username" to email
        )

        Walk_ApiClient.roomService.bookRoom(req)
            .enqueue(object : Callback<Walk_ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<Walk_ApiResponse<Any>>,
                    response: Response<Walk_ApiResponse<Any>>
                ) {
                    Toast.makeText(
                        this@Walk_RoomListActivity,
                        response.body()?.message ?: "",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadRooms()
                }

                override fun onFailure(call: Call<Walk_ApiResponse<Any>>, t: Throwable) {
                    Toast.makeText(this@Walk_RoomListActivity, "Error booking room", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun unreserveRoom(room: Walk_Room) {
        val req = mapOf("room" to room.room)

        Walk_ApiClient.roomService.unbookRoom(req)
            .enqueue(object : Callback<Walk_ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<Walk_ApiResponse<Any>>,
                    response: Response<Walk_ApiResponse<Any>>
                ) {
                    Toast.makeText(
                        this@Walk_RoomListActivity,
                        response.body()?.message ?: "",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadRooms()
                }

                override fun onFailure(call: Call<Walk_ApiResponse<Any>>, t: Throwable) {
                    Toast.makeText(this@Walk_RoomListActivity, "Error unbooking room", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
