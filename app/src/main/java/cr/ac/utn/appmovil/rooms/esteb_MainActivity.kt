package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import Service.esteb_APIService
import kotlinx.coroutines.launch
import model.esteb_RoomRequest
import util.util

class esteb_MainActivity : AppCompatActivity() {

    private lateinit var edtRoom: EditText
    private lateinit var edtCapacity: EditText
    private lateinit var btnCreate: Button
    private lateinit var btnList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_esteb_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtRoom = findViewById(R.id.edtRoom)
        edtCapacity = findViewById(R.id.edtCapacity)
        btnCreate = findViewById(R.id.btnCreate)
        btnList = findViewById(R.id.btnList)

        btnCreate.setOnClickListener {
            val room = edtRoom.text.toString().trim()
            val capacityText = edtCapacity.text.toString().trim()

            if (room.isEmpty() || capacityText.isEmpty()) {
                Toast.makeText(this, getString(R.string.esteb_MsgInvalidationData), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val capacity = capacityText.toIntOrNull()
            if (capacity == null) {
                Toast.makeText(this, getString(R.string.esteb_MsgInvalidationDataNumber), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createRoom(room, capacity)
        }

        btnList.setOnClickListener {
            util.openActivity(this, esteb_ListRoomActivity::class.java)
        }
    }

    private fun createRoom(room: String, capacity: Int) {
        lifecycleScope.launch {
            try {
                val request = esteb_RoomRequest(room, capacity)
                val response = esteb_APIService.api.createRoom(request)

                if (response.data != null) {
                    Toast.makeText(
                        this@esteb_MainActivity,
                        "The room was create: ${response.data.room}",
                        Toast.LENGTH_LONG
                    ).show()

                    edtRoom.text.clear()
                    edtCapacity.text.clear()

                } else {
                    Toast.makeText(
                        this@esteb_MainActivity,
                        response.message ?: getString(R.string.esteb_Error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@esteb_MainActivity,
                    getString(R.string.esteb_Error),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
}