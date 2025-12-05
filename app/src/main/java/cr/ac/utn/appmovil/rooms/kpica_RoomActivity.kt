package cr.ac.utn.appmovil.rooms

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kpica_Controller.kpica_RoomController

class kpica_RoomActivity : AppCompatActivity() {

    private lateinit var kpica_RoomController: kpica_RoomController

    private lateinit var mycontext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.kpica_room_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kpica_TableLayout_room)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mycontext = this
        kpica_RoomController = kpica_RoomController(mycontext)

        getRoom()
    }

    private fun getRoom() {
        lifecycleScope.launch {
            try {
                val rooms = kpica_RoomController.getRoom()
                if (rooms.isNotEmpty()) {

                }
            } catch (e: Exception) {
                Toast.makeText(mycontext, e.message.toString(),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}