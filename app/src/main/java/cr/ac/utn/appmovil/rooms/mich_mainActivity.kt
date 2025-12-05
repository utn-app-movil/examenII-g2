package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import util.util

class mich_mainActivity : AppCompatActivity() {

    private var username: String = "estudiante"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mich_main)

        username = intent.getStringExtra("username") ?: "estudiante"

        val btnGoCreate = findViewById<Button>(R.id.btnGoCreateRoom)
        val btnGoList = findViewById<Button>(R.id.btnGoRoomList)

        btnGoCreate.setOnClickListener {
            util.openActivity(this, mich_roomActivity::class.java)
        }

        btnGoList.setOnClickListener {
            util.openActivity(this, mich_roomListActivity::class.java, "username", username)
        }
    }
}
