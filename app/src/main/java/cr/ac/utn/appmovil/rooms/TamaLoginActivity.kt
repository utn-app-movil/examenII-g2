package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.appmovil.rooms.R.*
import kotlinx.coroutines.launch
import model.tama_AuthRequest
import tamanetwork.TamaApiProvider
import util.util
import kotlinx.coroutines.delay

class TamaLoginActivity : AppCompatActivity() {

    private lateinit var tama_etUser: EditText
    private lateinit var tama_etPassword: EditText
    private lateinit var tama_btnLogin: Button
    private lateinit var tama_tvMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_tama_login)

        tama_etUser = findViewById(id.tama_etUser)
        tama_etPassword = findViewById(id.tama_etPassword)
        tama_btnLogin = findViewById(id.tama_btnLogin)
        tama_tvMessage = findViewById(id.tama_tvMessage)

        tama_btnLogin.setOnClickListener {
            tama_doLogin()
        }
    }

    private fun tama_doLogin() {
        val tama_username = tama_etUser.text.toString().trim()
        val tama_password = tama_etPassword.text.toString().trim()

        if (tama_username.isEmpty() || tama_password.isEmpty()) {
            tama_tvMessage.text = getString(string.tama_msg_incomplete)
            return
        }

        tama_tvMessage.text = "Intentando iniciar sesión..."

        lifecycleScope.launch {
            try {
                val tama_request = tama_AuthRequest(
                    tama_username = tama_username,
                    tama_password = tama_password
                )

                val tama_response = TamaApiProvider.tama_api.tama_authUser(tama_request)

                if (tama_response.isSuccessful) {
                    val tama_body = tama_response.body()
                    if (tama_body != null) {

                        tama_tvMessage.text = tama_body.message ?: "¡Login Exitoso!"


                        delay(1000)


                        util.openActivity(
                            this@TamaLoginActivity,
                            TamaRoomsActivity::class.java
                        )
                        finish()
                    } else {
                        tama_tvMessage.text = "Respuesta vacía del servidor"
                    }
                } else {

                    val errorText = tama_response.errorBody()?.string()
                    tama_tvMessage.text = "Error HTTP: ${tama_response.code()}\n$errorText"
                }
            } catch (e: Exception) {

                tama_tvMessage.text = "Error de conexión: ${e.localizedMessage}"
            }
        }
    }
}