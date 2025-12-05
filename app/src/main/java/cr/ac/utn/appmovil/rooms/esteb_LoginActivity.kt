package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import Service.esteb_APIService
import android.widget.EditText
import model.esteb_AuthRequest
import util.util

class esteb_LoginActivity : AppCompatActivity() {
    private lateinit var edtUsuario: EditText
    private lateinit var edtContrasena: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_esteb_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtUsuario = findViewById(R.id.edtUsuario)
        edtContrasena = findViewById(R.id.edtContrasena)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username = edtUsuario.text.toString().trim()
            val password = edtContrasena.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                getString(R.string.esteb_MsgInvalidationData),
                Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(username, password)
        }
    }

    fun loginUser(username: String, password: String) {
        lifecycleScope.launch {
            try {

                val request = esteb_AuthRequest(username, password)
                val response = esteb_APIService.api.login(request)

                if (response.data != null) {
                    val name = response.data.name
                    val lastname = response.data.lastname

                    Toast.makeText(
                        this@esteb_LoginActivity,
                        "Welcome $name $lastname",
                        Toast.LENGTH_LONG
                    ).show()

                    util.openActivity(this@esteb_LoginActivity, esteb_MainActivity::class.java)

                } else {
                    Toast.makeText(
                        this@esteb_LoginActivity,
                        response.message ?: getString(R.string.esteb_MsgIncorrectCredentials),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@esteb_LoginActivity,
                    getString(R.string.esteb_Error),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
}