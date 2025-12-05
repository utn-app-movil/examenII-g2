package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import interfaces.frank_IApiService
import model.frank_ApiResponse
import model.frank_AuthRequest
import model.frank_User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import service.frank_ApiService

class frank_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frank_activity_login)

        val usernameEditText = findViewById<EditText>(R.id.frank_username)
        val passwordEditText = findViewById<EditText>(R.id.frank_password)
        val loginButton = findViewById<Button>(R.id.frank_login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val authRequest = frank_AuthRequest(username, password)
                frank_ApiService.instance.auth(authRequest).enqueue(object : Callback<frank_ApiResponse<Unit>> {
                    override fun onResponse(call: Call<frank_ApiResponse<Unit>>, response: Response<frank_ApiResponse<Unit>>) {
                        // Corrected the expected success code from "SUCESSFUL" to "INFO_FOUND"
                        if (response.isSuccessful && response.body()?.responseCode == "INFO_FOUND") {
                            fetchUserAndProceed(username)
                        } else {
                            val errorMessage = response.body()?.message ?: "Credenciales inválidas o error de respuesta"
                            Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<frank_ApiResponse<Unit>>, t: Throwable) {
                        Toast.makeText(applicationContext, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(applicationContext, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserAndProceed(username: String) {
        frank_ApiService.instance.getUsers().enqueue(object : Callback<frank_ApiResponse<List<frank_User>>> {
            override fun onResponse(call: Call<frank_ApiResponse<List<frank_User>>>, response: Response<frank_ApiResponse<List<frank_User>>>) {
                if (response.isSuccessful && response.body()?.data != null) {
                    val user = response.body()!!.data!!.find { it.username == username }
                    if (user != null) {
                        Toast.makeText(applicationContext, "Login exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@frank_LoginActivity, frank_RoomsActivity::class.java).apply {
                            putExtra("FRANK_USER_EMAIL", user.email)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Usuario autenticado, pero no se encontraron sus detalles.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Falló la obtención de los detalles del usuario.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<frank_ApiResponse<List<frank_User>>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de red al buscar usuario: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}