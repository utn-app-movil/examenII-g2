package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import Service.ahi_RoomRepository
import util.util
import android.widget.*
import util.EXTRA_ID

class ahi_AuthenticationActivity : AppCompatActivity() {

    private val repository = ahi_RoomRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahi_authentication)

        val etUser = findViewById<EditText>(R.id.et_ahi_username)
        val etPass = findViewById<EditText>(R.id.et_ahi_password)
        val btnLogin = findViewById<Button>(R.id.btn_ahi_login)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val (success, message) = repository.login(user, pass)
                Toast.makeText(this@ahi_AuthenticationActivity, message, Toast.LENGTH_LONG).show()

                if (success) {
                    val realEmail = repository.currentUserEmail
                        ?: "estudiante@est.utn.ac.cr"  // solo por seguridad extrema

                    util.openActivity(
                        this@ahi_AuthenticationActivity,
                        ahi_RoomListActivity::class.java,
                        EXTRA_ID,
                        realEmail
                    )
                    finish()
                }
            }
        }
    }
}