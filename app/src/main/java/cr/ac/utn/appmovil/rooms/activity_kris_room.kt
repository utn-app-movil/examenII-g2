package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.rooms.kris_repository.kris_RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class kris_room_activity : AppCompatActivity() {

    private lateinit var kris_etName: EditText
    private lateinit var kris_etCapacity: EditText
    private lateinit var kris_btnCreate: Button

    private val kris_roomRepository = kris_RoomRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kris_room)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kris_room_create_main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        kris_etName = findViewById(R.id.kris_etRoomName)
        kris_etCapacity = findViewById(R.id.kris_etRoomCapacity)
        kris_btnCreate = findViewById(R.id.kris_btnCreateRoom)

        kris_btnCreate.setOnClickListener {
            val name = kris_etName.text.toString().trim()
            val capacityStr = kris_etCapacity.text.toString().trim()

            if (name.isEmpty() || capacityStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.kris_room_create_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val capacity = capacityStr.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                Toast.makeText(this, getString(R.string.kris_room_create_invalid_capacity), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            kris_createRoom(name, capacity)
        }
    }

    private fun kris_createRoom(name: String, capacity: Int) {
        kris_btnCreate.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = kris_roomRepository.createRoom(name, capacity)
                withContext(Dispatchers.Main) {
                    kris_btnCreate.isEnabled = true
                    if (response.isSuccessful) {
                        val body = response.body()
                        val code = body?.responseCode
                        val message = body?.message ?: getString(R.string.kris_generic_error)

                        if (code == "SUCESSFUL") {
                            Toast.makeText(this@kris_room_activity, message, Toast.LENGTH_LONG).show()

                            kris_etName.text.clear()
                            kris_etCapacity.text.clear()
                        } else {
                            Toast.makeText(this@kris_room_activity, message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(
                            this@kris_room_activity,
                            getString(R.string.kris_http_error, response.code()),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    kris_btnCreate.isEnabled = true
                    Toast.makeText(
                        this@kris_room_activity,
                        getString(R.string.kris_exception_error, ex.localizedMessage ?: "Unknown"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
