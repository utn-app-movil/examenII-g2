package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import util.util
import android.widget.TextView

class mich_mainActivity : AppCompatActivity() {

    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mich_main)

        email = intent.getStringExtra("email") ?: ""

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnGoCreateRoom = findViewById<Button>(R.id.btnGoCreateRoom)
        val btnGoRoomList = findViewById<Button>(R.id.btnGoRoomList)

        // Solo para mostrar algo, no es obligatorio
        tvWelcome.text = "Módulo Michael - $email"

        btnGoCreateRoom.setOnClickListener {
            util.openActivity(this, mich_roomActivity::class.java, "email", email)
        }

        btnGoRoomList.setOnClickListener {
            util.openActivity(this, mich_roomListActivity::class.java, "email", email)
        }
    }
}