package cr.ac.utn.appmovil.rooms

import Service.fer_APIService
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.fer_AuthRequest
import util.util

class fer_AuthenticationActivity : AppCompatActivity() {

    private lateinit var fer_etUsername: EditText
    private lateinit var fer_etPassword: EditText
    private lateinit var fer_btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fer_activity_authentication)

        fer_etUsername = findViewById(R.id.fer_et_username)
        fer_etPassword = findViewById(R.id.fer_et_password)
        fer_btnLogin = findViewById(R.id.fer_btn_login)

        fer_btnLogin.setOnClickListener {
            fer_performLogin()
        }
    }

    private fun fer_performLogin() {
        val username = fer_etUsername.text.toString().trim()
        val password = fer_etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.fer_error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val authRequest = fer_AuthRequest(username, password)

        lifecycleScope.launch {
            try {
                val response = fer_APIService.apiService.authenticateUser(authRequest)

                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    if (apiResponse != null) {
                        when (apiResponse.responseCode) {
                            "INFO_FOUND", "SUCESSFUL" -> {
                                Toast.makeText(
                                    this@fer_AuthenticationActivity,
                                    apiResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Navigate to room list
                                util.openActivity(
                                    this@fer_AuthenticationActivity,
                                    fer_RoomListActivity::class.java,
                                    "username",
                                    username
                                )
                                finish()
                            }
                            else -> {
                                Toast.makeText(
                                    this@fer_AuthenticationActivity,
                                    apiResponse.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this@fer_AuthenticationActivity,
                        getString(R.string.fer_error_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@fer_AuthenticationActivity,
                    "${getString(R.string.fer_error_exception)}: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
