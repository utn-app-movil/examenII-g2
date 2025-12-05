package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sebas_controller.RoomController

class SebasRoomActivity : AppCompatActivity() {
    
    private lateinit var etRoomName: EditText
    private lateinit var etRoomCapacity: EditText
    private lateinit var btnCreateRoom: Button
    private lateinit var btnCancel: Button
    private lateinit var tvMessage: TextView
    private lateinit var pbRoom: ProgressBar
    private lateinit var roomController: RoomController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sebas_activity_room)
        
        roomController = RoomController(this)
        
        etRoomName = findViewById(R.id.sebas_et_room_name)
        etRoomCapacity = findViewById(R.id.sebas_et_room_capacity)
        btnCreateRoom = findViewById(R.id.sebas_btn_create_room)
        btnCancel = findViewById(R.id.sebas_btn_cancel_room)
        tvMessage = findViewById(R.id.sebas_tv_room_message)
        pbRoom = findViewById(R.id.sebas_pb_room)
        
        btnCreateRoom.setOnClickListener {
            val roomName = etRoomName.text.toString()
            val capacityStr = etRoomCapacity.text.toString()
            
            if (roomName.isEmpty() || capacityStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.sebas_error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val capacity = capacityStr.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                Toast.makeText(this, "Invalid capacity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            pbRoom.visibility = ProgressBar.VISIBLE
            btnCreateRoom.isEnabled = false
            
            lifecycleScope.launch {
                try {
                    roomController.createRoom(roomName, capacity)
                    
                    pbRoom.visibility = ProgressBar.GONE
                    btnCreateRoom.isEnabled = true
                    
                    Toast.makeText(this@SebasRoomActivity, getString(R.string.sebas_success_room_created), Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    pbRoom.visibility = ProgressBar.GONE
                    btnCreateRoom.isEnabled = true
                    tvMessage.text = e.message
                    tvMessage.visibility = TextView.VISIBLE
                    Toast.makeText(this@SebasRoomActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        
        btnCancel.setOnClickListener {
            finish()
        }
    }
}
