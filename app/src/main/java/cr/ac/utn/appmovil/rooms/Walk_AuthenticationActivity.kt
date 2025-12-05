package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Walk_AuthenticationActivity : AppCompatActivity() {

    private lateinit var userInput: EditText
    private lateinit var passInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walk_activity_authentication)

        userInput = findViewById(R.id.walk_login_user)
        passInput = findViewById(R.id.walk_login_pass)
        loginButton = findViewById(R.id.walk_login_button)

        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = userInput.text.toString().trim()
        val password = passInput.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = mapOf(
            "username" to username,
            "password" to password
        )

        Walk_ApiClient.authService.login(request)
            .enqueue(object : Callback<Walk_LoginResponse> {
                override fun onResponse(
                    call: Call<Walk_LoginResponse>,
                    response: Response<Walk_LoginResponse>
                ) {
                    val body = response.body()

                    if (body == null) {
                        Toast.makeText(
                            this@Walk_AuthenticationActivity,
                            "Invalid response from server",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    // 🔥 LOGIN CORRECTO -> INFO_FOUND
                    if (body.responseCode == "INFO_FOUND" && body.data != null) {

                        // Guardamos el usuario devuelto del login
                        val u = body.data!!

                        Walk_Session.currentUser = Walk_User(
                            username = u.user,
                            name = u.name,
                            lastname = u.lastname,
                            email = u.email,
                            isActive = true
                        )

                        Toast.makeText(
                            this@Walk_AuthenticationActivity,
                            "Welcome ${u.name}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Ir a lista de salas
                        startActivity(
                            Intent(
                                this@Walk_AuthenticationActivity,
                                Walk_RoomListActivity::class.java
                            )
                        )

                        finish()

                    } else {
                        Toast.makeText(
                            this@Walk_AuthenticationActivity,
                            body.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Walk_LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@Walk_AuthenticationActivity,
                        "Network error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
