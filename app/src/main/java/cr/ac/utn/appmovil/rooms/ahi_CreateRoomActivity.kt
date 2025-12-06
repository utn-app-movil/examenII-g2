package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import Service.ahi_RoomRepository
import android.widget.*
import util.util

class ahi_CreateRoomActivity : AppCompatActivity() {

    private val repo = ahi_RoomRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahi_create_room)

        val etName = findViewById<EditText>(R.id.et_ahi_room_name)
        val etCap = findViewById<EditText>(R.id.et_ahi_room_capacity)
        val btnSave = findViewById<Button>(R.id.btn_ahi_save_room)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val capStr = etCap.text.toString().trim()
            if (name.isEmpty() || capStr.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val capacity = capStr.toIntOrNull() ?: run {
                Toast.makeText(this, "Capacidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val msg = repo.createRoom(name, capacity)
                Toast.makeText(this@ahi_CreateRoomActivity, msg, Toast.LENGTH_LONG).show()
                if (msg.contains("sucessfully", true)) {
                    finish()
                }
            }
        }
    }
}