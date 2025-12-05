package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.rooms.kris_AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlin.jvm.java

class kris_authentication_activity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    private val authRepository = kris_AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kris_authentication)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)


        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.kris_login_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            doLogin(username, password)
        }
    }

    private fun doLogin(username: String, password: String) {
        btnLogin.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = authRepository.authenticate(username, password)

                withContext(Dispatchers.Main) {
                    btnLogin.isEnabled = true

                    if (response.isSuccessful) {
                        val body = response.body()
                        val code = body?.responseCode
                        val message = body?.message ?: getString(R.string.kris_login_error_generic)

                        if (code == "INFO_FOUND" || code == "ACTION_SUCCESS" || code == "SUCCESS") {
                            Toast.makeText(this@kris_authentication_activity, message, Toast.LENGTH_LONG).show()

                            val intent = Intent(
                                this@kris_authentication_activity,activity_kris_room_list::class.java
                            )
                            startActivity(intent)
                            finish()


                        } else {
                            Toast.makeText(this@kris_authentication_activity, message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(
                            this@kris_authentication_activity,
                            getString(R.string.kris_login_error_http, response.code()),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    btnLogin.isEnabled = true
                    Toast.makeText(
                        this@kris_authentication_activity,
                        getString(R.string.kris_login_error_exception, ex.localizedMessage ?: "Unknown"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
