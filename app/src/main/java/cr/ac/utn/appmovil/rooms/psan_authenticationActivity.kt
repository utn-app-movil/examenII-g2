package cr.ac.utn.appmovil.rooms

import Service.psan_APIService
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import model.psan_userAuthRequest
import util.util

class psan_authenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psan_authentication)

        val txtUsername = findViewById<EditText>(R.id.txtUsername)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val user = txtUsername.text.toString().trim()
            val pass = txtPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Both fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            doLogin(user, pass)
        }
    }

    private fun doLogin(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = psan_APIService.api.psan_authUser(
                    psan_userAuthRequest(username, password)
                )

                runOnUiThread {
                    // CORRECCIÓN: responseCode correcto según API real
                    if (response.body()?.responseCode == "INFO_FOUND") {
                        Toast.makeText(
                            this@psan_authenticationActivity,
                            "Login OK",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Redirigir a la lista de salas
                        util.openActivity(
                            this@psan_authenticationActivity,
                            psan_roomListActivity::class.java
                        )

                    } else {
                        Toast.makeText(
                            this@psan_authenticationActivity,
                            response.body()?.message ?: "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@psan_authenticationActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
