package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import Service.emur_APIService
import kotlinx.coroutines.launch
import model.emur_CreateRoomRequest
import util.util
import cr.ac.utn.appmovil.rooms.R


class emur_RoomActivity : AppCompatActivity() {

    private lateinit var emur_roomNameEditText: EditText
    private lateinit var emur_capacityEditText: EditText
    private lateinit var emur_createRoomButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emur_room)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emur_roomNameEditText = findViewById(R.id.emur_roomNameEditText)
        emur_capacityEditText = findViewById(R.id.emur_capacityEditText)
        emur_createRoomButton = findViewById(R.id.emur_createRoomButton)

        emur_createRoomButton.setOnClickListener {
            emur_createRoom()
        }
    }

    private fun emur_createRoom() {
        val roomName = emur_roomNameEditText.text.toString()
        val capacityStr = emur_capacityEditText.text.toString()

        if (roomName.isBlank() || capacityStr.isBlank()) {
            util.showDialogCondition(this,
                getString(R.string.emur_api_error_title),
                "El nombre y la capacidad de la sala no pueden estar vacíos.",
                "OK", "Cancelar", {}, {})
            return
        }

        val capacity = capacityStr.toIntOrNull()
        if (capacity == null || capacity <= 0) {
            util.showDialogCondition(this,
                getString(R.string.emur_api_error_title),
                "La capacidad debe ser un número entero positivo.",
                "OK", "Cancelar", {}, {})
            return
        }

        lifecycleScope.launch {
            try {
                val roomRequest = emur_CreateRoomRequest(roomName, capacity)
                val response = emur_APIService.emur_apiRooms.createRoom(roomRequest)

                if (response.isSuccessful) {
                    val baseResponse = response.body()
                    val message = baseResponse?.message ?: "Sala creada exitosamente."

                    util.showDialogCondition(this@emur_RoomActivity,
                        "Creación Exitosa",
                        message,
                        "OK", "Cancelar", { finish() }, {}) // Cierra la pantalla si es exitoso

                } else {
                    util.showDialogCondition(this@emur_RoomActivity,
                        getString(R.string.emur_api_error_title),
                        "Error al crear la sala: Código ${response.code()}.",
                        "OK", "Cancelar", {}, {})
                }
            } catch (e: Exception) {
                util.showDialogCondition(this@emur_RoomActivity,
                    getString(R.string.emur_api_error_title),
                    "Error de red/servidor: ${e.localizedMessage}",
                    "OK", "Cancelar", {}, {})
            }
        }
    }
}