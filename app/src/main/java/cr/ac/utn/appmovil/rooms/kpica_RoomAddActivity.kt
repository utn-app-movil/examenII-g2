package cr.ac.utn.appmovil.rooms

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kpica_Controller.kpica_RoomController
import kpica_Entity.kpica_Room
import util.util
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class kpica_RoomAddActivity : AppCompatActivity() {
    private lateinit var txtRoomName: EditText
    private lateinit var txtRoomCapacity: EditText

    private lateinit var roomController: kpica_RoomController
    private lateinit var mycontext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.kpica_room_add_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kpica_TableLayout_roomAdd)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mycontext = this
        roomController = kpica_RoomController(mycontext)

        txtRoomName = findViewById(R.id.kpica_txtRoomName)
        txtRoomCapacity = findViewById(R.id.kpica_txtRoomCapacity)

        val  btn_CancelRoom = findViewById<Button>(R.id.kpica_btn_CancelRoom)
        btn_CancelRoom.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(mycontext, kpica_RoomActivity::class.java)
        })

        val btn_SaveRoom = findViewById<Button>(R.id.kpica_btn_SaveRoom)
        btn_SaveRoom.setOnClickListener(View.OnClickListener { view ->
            saveRoom()
        })
    }

    private fun saveRoom() {
        try {
            if (validate()) {
                val room = kpica_Room()
                room.Room = txtRoomName.text.toString()
                room.Capacity = txtRoomCapacity.text.toString().toInt()

                lifecycleScope.launch {
                    roomController.createRoom(room)
                }
                util.openActivity(mycontext, kpica_RoomActivity::class.java)
            } else {
                Toast.makeText(mycontext, getString(R.string.kpica_RoomIncomplete),
                    Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(mycontext, e.message,
                Toast.LENGTH_LONG).show()
        }
    }

    private fun validate(): Boolean {
        return !txtRoomName.text.isBlank() && !txtRoomCapacity.text.isNullOrBlank() &&
                txtRoomCapacity.text.toString().toInt() > 0
    }
}