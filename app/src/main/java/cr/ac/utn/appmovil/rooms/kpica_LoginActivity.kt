package cr.ac.utn.appmovil.rooms

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kpica_Controller.kpica_UserController
import kpica_Entity.kpica_User
import util.util

class kpica_LoginActivity : AppCompatActivity() {
    lateinit var txtUserName: EditText
    lateinit var txtUserPassword: EditText

    private lateinit var kpica_UserController: kpica_UserController

    lateinit var mycontext: Context

    object SessionManager {
        var user: kpica_User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.kpica_activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kpica_TableLayout_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mycontext = this
        kpica_UserController = kpica_UserController(mycontext)

        txtUserName = findViewById(R.id.kpica_txtUserName)
        txtUserPassword = findViewById(R.id.kpica_txtUserPassword)

        val buttonLogin = findViewById<Button>(R.id.kpica_BTN_Login)
        buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login(){
        var userName: String = txtUserName.text.toString()
        var password: String = txtUserPassword.text.toString()

        lifecycleScope.launch {
            try {
                val user = kpica_UserController.userLogin(userName, password)
                if (user != null) {
                    user.Username = userName
                    SessionManager.user = user
                    util.openActivity(mycontext, kpica_RoomActivity::class.java)
                } else {
                    Toast.makeText(mycontext, getString(R.string.kpica_ErrorLogin),
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(mycontext, e.message.toString(),
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}