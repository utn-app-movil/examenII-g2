package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import model.frank_ApiResponse
import model.frank_CreateRoomRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import service.frank_ApiService

class frank_CreateRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frank_activity_create_room)

        val roomNameEditText = findViewById<EditText>(R.id.frank_create_room_name)
        val capacityEditText = findViewById<EditText>(R.id.frank_create_room_capacity)
        val createButton = findViewById<Button>(R.id.frank_create_button)

        createButton.setOnClickListener {
            val roomName = roomNameEditText.text.toString().trim()
            val capacityStr = capacityEditText.text.toString().trim()

            if (roomName.isEmpty() || capacityStr.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val capacity = capacityStr.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                Toast.makeText(this, "Por favor, ingrese una capacidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            frank_createRoom(roomName, capacity)
        }
    }

    private fun frank_createRoom(roomName: String, capacity: Int) {
        val request = frank_CreateRoomRequest(roomName, capacity)
        frank_ApiService.instance.createRoom(request).enqueue(object : Callback<frank_ApiResponse<Unit>> {
            override fun onResponse(call: Call<frank_ApiResponse<Unit>>, response: Response<frank_ApiResponse<Unit>>) {
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    Toast.makeText(applicationContext, apiResponse.message, Toast.LENGTH_LONG).show()
                    
                    if (apiResponse.responseCode == "SUCESSFUL") {
                        finish()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error al crear la sala", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<frank_ApiResponse<Unit>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}