package cr.ac.utn.appmovil.rooms

import Service.fer_APIService
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.fer_RoomRequest

class fer_RoomActivity : AppCompatActivity() {

    private lateinit var fer_etRoomName: EditText
    private lateinit var fer_etCapacity: EditText
    private lateinit var fer_btnCreate: Button
    private lateinit var fer_btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fer_activity_room)

        fer_etRoomName = findViewById(R.id.fer_et_room_name)
        fer_etCapacity = findViewById(R.id.fer_et_capacity)
        fer_btnCreate = findViewById(R.id.fer_btn_create_room)
        fer_btnBack = findViewById(R.id.fer_btn_back)

        fer_btnCreate.setOnClickListener {
            fer_createRoom()
        }

        fer_btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fer_createRoom() {
        val roomName = fer_etRoomName.text.toString().trim()
        val capacityStr = fer_etCapacity.text.toString().trim()

        if (roomName.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.fer_error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val capacity = capacityStr.toIntOrNull()
        if (capacity == null || capacity <= 0) {
            Toast.makeText(this, getString(R.string.fer_error_invalid_capacity), Toast.LENGTH_SHORT).show()
            return
        }

        val roomRequest = fer_RoomRequest(roomName, capacity)

        lifecycleScope.launch {
            try {
                val response = fer_APIService.apiService.createRoom(roomRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null) {
                        Toast.makeText(
                            this@fer_RoomActivity,
                            apiResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (apiResponse.responseCode == "SUCESSFUL") {
                            fer_etRoomName.setText("")
                            fer_etCapacity.setText("")
                        }
                    }
                } else {
                    Toast.makeText(
                        this@fer_RoomActivity,
                        getString(R.string.fer_error_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@fer_RoomActivity,
                    "${getString(R.string.fer_error_exception)}: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
