package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import Service.knu_APIService
import cr.ac.utn.appmovil.rooms.model.knu_LoginRequest
import cr.ac.utn.appmovil.rooms.model.knu_LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.util

class knu_AuthenticationActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtMessage: TextView
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knu_authentication)

        txtUser = findViewById(R.id.knu_txtUsername)
        txtPassword = findViewById(R.id.knu_txtPassword)
        txtMessage = findViewById(R.id.knu_txtLoginMessage)
        btnLogin = findViewById(R.id.knu_btnLogin)

        btnLogin.setOnClickListener {
            knu_doLogin()
        }
    }

    private fun knu_doLogin() {
        val username = txtUser.text.toString().trim()
        val password = txtPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show()
            return
        }

        val request = knu_LoginRequest(username, password)

        knu_APIService.api.knu_login(request)
            .enqueue(object : Callback<knu_LoginResponse> {
                override fun onResponse(
                    call: Call<knu_LoginResponse>,
                    response: Response<knu_LoginResponse>
                ) {
                    if (!response.isSuccessful) {
                        txtMessage.text = "Error: ${response.code()}"
                        return
                    }

                    val body = response.body()
                    if (body == null) {
                        txtMessage.text = "Empty response"
                        return
                    }

                    // Mostrar el mensaje del API siempre
                    txtMessage.text = body.message

                    if (body.responseCode == "INFO_FOUND" && body.data != null) {
                        // Usar el CORREO como identificador principal
                        var email = body.data.email
                        if (email.isNullOrBlank()) {
                            email = body.data.user
                        }

                        txtMessage.text = "Welcome ${body.data.name} ${body.data.lastname}\n$email"

                        // Ir a la lista de salas pasando el correo como knu_username
                        util.openActivity(
                            this@knu_AuthenticationActivity,
                            knu_RoomListActivity::class.java,
                            "knu_username",
                            email
                        )

                        finish()
                    }
                }

                override fun onFailure(call: Call<knu_LoginResponse>, t: Throwable) {
                    txtMessage.text = "Failure: ${t.message}"
                }
            })
    }
}
