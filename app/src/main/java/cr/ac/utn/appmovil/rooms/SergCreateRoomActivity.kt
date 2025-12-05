package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.model.SergApiResponse
import cr.ac.utn.appmovil.rooms.model.SergCreateRoomRequest
import cr.ac.utn.appmovil.rooms.model.SergRoom
import cr.ac.utn.appmovil.rooms.service.SergAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.util

class SergCreateRoomActivity : AppCompatActivity() {

    private lateinit var etRoomName: EditText
    private lateinit var etCapacity: EditText
    private lateinit var btnCreate: Button
    private lateinit var btnViewRooms: Button
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serg_create_room)

        username = intent.getStringExtra("username") ?: ""

        etRoomName = findViewById(R.id.serg_et_room_name)
        etCapacity = findViewById(R.id.serg_et_capacity)
        btnCreate = findViewById(R.id.serg_btn_create_room)
        btnViewRooms = findViewById(R.id.serg_btn_view_rooms)

        btnCreate.setOnClickListener {
            val roomName = etRoomName.text.toString()
            val capacityStr = etCapacity.text.toString()

            if (roomName.isEmpty() || capacityStr.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val capacity = capacityStr.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                Toast.makeText(this, "Invalid capacity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createRoom(roomName, capacity)
        }

        btnViewRooms.setOnClickListener {
            util.openActivity(this, SergRoomsListActivity::class.java, "username", username)
        }
    }

    private fun createRoom(roomName: String, capacity: Int) {
        val request = SergCreateRoomRequest(roomName, capacity)
        SergAPIService.api.createRoom(request).enqueue(object : Callback<SergApiResponse<SergRoom>> {
            override fun onResponse(call: Call<SergApiResponse<SergRoom>>, response: Response<SergApiResponse<SergRoom>>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    Toast.makeText(this@SergCreateRoomActivity, apiResponse.message, Toast.LENGTH_SHORT).show()
                    if (apiResponse.responseCode == "SUCESSFUL") {
                        etRoomName.text.clear()
                        etCapacity.text.clear()
                    }
                }
            }

            override fun onFailure(call: Call<SergApiResponse<SergRoom>>, t: Throwable) {
                Toast.makeText(this@SergCreateRoomActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}