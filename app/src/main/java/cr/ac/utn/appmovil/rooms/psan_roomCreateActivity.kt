package cr.ac.utn.appmovil.rooms

import Service.psan_APIService
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import model.psan_roomCreateRequest

class psan_roomCreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psan_room_create)

        val txtRoom = findViewById<EditText>(R.id.txtRoom)
        val txtCapacity = findViewById<EditText>(R.id.txtCapacity)
        val btnCreate = findViewById<Button>(R.id.btnCreateRoom)

        btnCreate.setOnClickListener {
            val room = txtRoom.text.toString()
            val cap = txtCapacity.text.toString()

            if (room.isEmpty() || cap.isEmpty()) {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createRoom(room, cap.toInt())
        }
    }

    private fun createRoom(room: String, capacity: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = psan_APIService.api.psan_createRoom(psan_roomCreateRequest(room, capacity))

            runOnUiThread {
                Toast.makeText(this@psan_roomCreateActivity, response.body()?.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
