package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.Service.carl_RetrofitClient
import cr.ac.utn.appmovil.rooms.model.carl_CreateRoomRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class carl_CreateRoomActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextCapacity: EditText
    private lateinit var buttonCreate: Button
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carl_activity_create_room)

        editTextName = findViewById(R.id.carl_editTextRoomName)
        editTextCapacity = findViewById(R.id.carl_editTextCapacity)
        buttonCreate = findViewById(R.id.carl_buttonCreateRoom)
        buttonBack = findViewById(R.id.carl_buttonBack)

        buttonCreate.setOnClickListener {
            createRoom()
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun createRoom() {
        val name = editTextName.text.toString()
        val capacityStr = editTextCapacity.text.toString()

        if (name.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val capacity = capacityStr.toIntOrNull()

        if (capacity == null) {
            Toast.makeText(this, "Capacity must be a number", Toast.LENGTH_SHORT).show()
            return
        }

        val request = carl_CreateRoomRequest(
            room = name,
            capacity = capacity
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = carl_RetrofitClient.apiService.createRoom(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Toast.makeText(this@carl_CreateRoomActivity, apiResponse?.message ?: "Room created", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@carl_CreateRoomActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@carl_CreateRoomActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}