package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.wils_CreateRoomRequest
import Service.wils_APIService

class wils_RoomActivity : wils_BaseActivity() {
    
    private lateinit var etRoomName: EditText
    private lateinit var etCapacity: EditText
    private lateinit var btnCreateRoom: Button
    private lateinit var btnBack: Button
    private val apiService = wils_APIService()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wils_room)
        
        initViews()
        setupClickListeners()
    }
    
    private fun initViews() {
        etRoomName = findViewById(R.id.wils_etRoomName)
        etCapacity = findViewById(R.id.wils_etCapacity)
        btnCreateRoom = findViewById(R.id.wils_btnCreateRoom)
        btnBack = findViewById(R.id.wils_btnBack)
        tvMessage = findViewById(R.id.wils_tvMessage)
    }
    
    private fun setupClickListeners() {
        btnCreateRoom.setOnClickListener {
            val roomName = etRoomName.text.toString().trim()
            val capacityStr = etCapacity.text.toString().trim()
            
            if (roomName.isNotEmpty() && capacityStr.isNotEmpty()) {
                val capacity = capacityStr.toIntOrNull()
                if (capacity != null && capacity > 0) {
                    createRoom(roomName, capacity)
                } else {
                    showMessage(getString(R.string.wils_valid_capacity))
                }
            } else {
                showMessage(getString(R.string.wils_fill_all_fields))
            }
        }
        
        btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun createRoom(roomName: String, capacity: Int) {
        btnCreateRoom.isEnabled = false
        showMessage(getString(R.string.wils_creating_room))
        
        val context = this
        lifecycleScope.launch {
            try {
                val createRequest = wils_CreateRoomRequest(roomName, capacity)
                val response = apiService.createRoom(createRequest)
                
                if (response.responseCode == "SUCESSFUL" || response.message.contains("successfully")) {
                    showMessage(getString(R.string.wils_room_created_success))
                    Toast.makeText(context, getString(R.string.wils_room_created_success), Toast.LENGTH_SHORT).show()
                    

                    etRoomName.text.clear()
                    etCapacity.text.clear()
                } else {
                    showMessage(response.message)
                }
            } catch (e: Exception) {
                showMessage("Error creando sala: ${e.message}")
            } finally {
                btnCreateRoom.isEnabled = true
            }
        }
    }
}