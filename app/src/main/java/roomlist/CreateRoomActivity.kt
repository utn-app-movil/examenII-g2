package cr.ac.utn.appmovil.rooms.roomlist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import cr.ac.utn.appmovil.rooms.R

import cr.ac.utn.appmovil.rooms.viewmodel.meg_CreateRoomViewModel

class meg_CreateRoomActivity : ComponentActivity() {
    private val vm: meg_CreateRoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meg_createroom_activity)

        val txtName = findViewById<EditText>(R.id.txtRoomName)
        val txtCapacity = findViewById<EditText>(R.id.txtRoomCapacity)
        val btnCreate = findViewById<Button>(R.id.btnCreateRoom)

        btnCreate.setOnClickListener {
            val name = txtName.text.toString()
            val capacity = txtCapacity.text.toString().toIntOrNull() ?: 0
            if (name.isNotEmpty() && capacity > 0) {
                vm.createRoom(name, capacity)
            } else {
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show()
            }
        }

        vm.message.observe(this) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
