package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.frank_RoomsAdapter
import model.frank_ApiResponse
import model.frank_BookingRequest
import model.frank_Room
import model.frank_UnbookingRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import service.frank_ApiService
import util.util

class frank_RoomsActivity : AppCompatActivity() {

    private lateinit var roomsRecyclerView: RecyclerView
    private lateinit var roomsAdapter: frank_RoomsAdapter
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frank_activity_rooms)

        userEmail = intent.getStringExtra("FRANK_USER_EMAIL") ?: ""
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Error: no se pudo obtener el email del usuario.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setupRecyclerView()

        findViewById<Button>(R.id.frank_create_room_button).setOnClickListener {
            startActivity(Intent(this, frank_CreateRoomActivity::class.java))
        }

        findViewById<Button>(R.id.frank_refresh_button).setOnClickListener {
            getRooms()
        }
    }

    override fun onResume() {
        super.onResume()
        getRooms()
    }

    private fun setupRecyclerView() {
        roomsRecyclerView = findViewById(R.id.frank_rooms_recycler_view)
        roomsRecyclerView.layoutManager = LinearLayoutManager(this)
        roomsAdapter = frank_RoomsAdapter(emptyList()) { room -> onRoomClicked(room) }
        roomsRecyclerView.adapter = roomsAdapter
    }

    private fun onRoomClicked(room: frank_Room) {
        val action = if (room.is_busy) "liberar" else "reservar"
        util.showDialogCondition(this, "Confirmar Acción", "¿Desea ${action} la sala ${room.room}?", "Sí", "No",
            positiveCallback = {
                if (room.is_busy) unbookRoom(room) else bookRoom(room)
            },
            negativeCallback = {}
        )
    }

    private fun getRooms() {
        frank_ApiService.instance.getRooms().enqueue(object : Callback<frank_ApiResponse<List<frank_Room>>> {
            override fun onResponse(call: Call<frank_ApiResponse<List<frank_Room>>>, response: Response<frank_ApiResponse<List<frank_Room>>>) {
                if (response.isSuccessful && response.body()?.data != null) {
                    roomsAdapter.updateData(response.body()!!.data!!)
                } else {
                    Toast.makeText(applicationContext, "No se pudieron cargar las salas.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<frank_ApiResponse<List<frank_Room>>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bookRoom(room: frank_Room) {
        val request = frank_BookingRequest(room.room, userEmail)
        frank_ApiService.instance.bookRoom(request).enqueue(object : Callback<frank_ApiResponse<Unit>> {
            override fun onResponse(call: Call<frank_ApiResponse<Unit>>, response: Response<frank_ApiResponse<Unit>>) {
                Toast.makeText(applicationContext, response.body()?.message ?: "Acción completada", Toast.LENGTH_SHORT).show()
                getRooms()
            }
            override fun onFailure(call: Call<frank_ApiResponse<Unit>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun unbookRoom(room: frank_Room) {
        val request = frank_UnbookingRequest(room.room)
        frank_ApiService.instance.unbookRoom(request).enqueue(object : Callback<frank_ApiResponse<Unit>> {
            override fun onResponse(call: Call<frank_ApiResponse<Unit>>, response: Response<frank_ApiResponse<Unit>>) {
                Toast.makeText(applicationContext, response.body()?.message ?: "Acción completada", Toast.LENGTH_SHORT).show()
                getRooms()
            }

            override fun onFailure(call: Call<frank_ApiResponse<Unit>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
