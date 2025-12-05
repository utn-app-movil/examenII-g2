package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TamaCreateRoomActivity : AppCompatActivity() {

    private lateinit var tama_etName: EditText
    private lateinit var tama_etCap: EditText
    private lateinit var tama_btnCreate: Button
    private lateinit var tama_tvMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tama_create_room)

        tama_etName = findViewById(R.id.tama_etRoomName)
        tama_etCap = findViewById(R.id.tama_etRoomCapacity)
        tama_btnCreate = findViewById(R.id.tama_btnCreateRoom)
        tama_tvMessage = findViewById(R.id.tama_tvRoomCreateMsg)

        tama_btnCreate.setOnClickListener {
            tama_tvMessage.text = "Sala creada"
        }
    }
}