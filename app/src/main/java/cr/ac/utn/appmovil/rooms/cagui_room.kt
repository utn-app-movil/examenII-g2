package cr.ac.utn.appmovil.rooms

import Service.cagui_APIService
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.cagui_RoomCreateRequest

class cagui_room : AppCompatActivity() {

    private lateinit var etRoomName: TextInputEditText
    private lateinit var etRoomDescription: TextInputEditText
    private lateinit var etRoomCapacity: TextInputEditText
    private lateinit var etRoomFloor: TextInputEditText
    private lateinit var etRoomBuilding: TextInputEditText
    private lateinit var btnCreateRoom: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cagui_activity_room)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etRoomName = findViewById(R.id.cagui_etRoomName)
        etRoomDescription = findViewById(R.id.cagui_etRoomDescription)
        etRoomCapacity = findViewById(R.id.cagui_etRoomCapacity)
        etRoomFloor = findViewById(R.id.cagui_etRoomFloor)
        etRoomBuilding = findViewById(R.id.cagui_etRoomBuilding)
        btnCreateRoom = findViewById(R.id.cagui_btnCreateRoom)
        progressBar = findViewById(R.id.cagui_progressBarRoom)
    }

    private fun setupListeners() {
        btnCreateRoom.setOnClickListener {
            createRoom()
        }
    }

    private fun createRoom() {
        val name = etRoomName.text.toString().trim()
        val capacityStr = etRoomCapacity.text.toString().trim()

        // Validación básica solo para los campos requeridos por la API
        if (name.isEmpty() || capacityStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.cagui_fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val capacity = capacityStr.toIntOrNull()

        if (capacity == null) {
            Toast.makeText(this, "Please enter a valid number for capacity", Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Ajustado para enviar solo 'room' y 'capacity' como lo requiere la API y el modelo
                val roomRequest = cagui_RoomCreateRequest(
                    room = name,
                    capacity = capacity
                )
                val response = cagui_APIService.api.createRoom(roomRequest)

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    handleCreateResponse(response.responseCode, response.message)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@cagui_room,
                        getString(R.string.cagui_error_network),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleCreateResponse(responseCode: String, message: String) {
        when (responseCode) {
            "SUCESSFUL", "INFO_CREATED" -> {
                Toast.makeText(this, getString(R.string.cagui_room_created_success), Toast.LENGTH_SHORT).show()
                clearFields()
                finish()
            }
            else -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearFields() {
        etRoomName.text?.clear()
        etRoomDescription.text?.clear()
        etRoomCapacity.text?.clear()
        etRoomFloor.text?.clear()
        etRoomBuilding.text?.clear()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnCreateRoom.isEnabled = !isLoading
        etRoomName.isEnabled = !isLoading
        etRoomDescription.isEnabled = !isLoading
        etRoomCapacity.isEnabled = !isLoading
        etRoomFloor.isEnabled = !isLoading
        etRoomBuilding.isEnabled = !isLoading
    }
}
