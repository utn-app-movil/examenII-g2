package cr.ac.utn.appmovil.rooms.ui.auth

import cr.ac.utn.appmovil.rooms.R

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import cr.ac.utn.appmovil.rooms.ui.roomlist.meg_RoomListActivity
import cr.ac.utn.appmovil.rooms.viewmodel.meg_AuthViewModel


class meg_AuthenticationActivity : ComponentActivity() {

    private val vm: meg_AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meg_authentication_activity)

        val user = findViewById<EditText>(R.id.meg_input_user)
        val pass = findViewById<EditText>(R.id.meg_input_password)
        val btn = findViewById<Button>(R.id.meg_btn_login)
        val msg = findViewById<TextView>(R.id.meg_text_message)

        btn.setOnClickListener {
            vm.login(user.text.toString().trim(), pass.text.toString().trim())
        }

        vm.message.observe(this) { msg.text = it ?: "" }

        vm.user.observe(this) { u ->
            if (u != null) {

                startActivity(Intent(this, meg_RoomListActivity::class.java))
                finish()
            }
        }
    }
}
