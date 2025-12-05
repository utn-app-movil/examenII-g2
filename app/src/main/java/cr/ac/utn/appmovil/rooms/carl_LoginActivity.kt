package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.Service.carl_RetrofitClient
import cr.ac.utn.appmovil.rooms.model.carl_LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class carl_LoginActivity : AppCompatActivity() {

    private lateinit var editTextUser: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carl_activity_login)

        editTextUser = findViewById(R.id.carl_editTextUser)
        editTextPassword = findViewById(R.id.carl_editTextPassword)
        buttonLogin = findViewById(R.id.carl_buttonLogin)

        buttonLogin.setOnClickListener {
            val user = editTextUser.text.toString()
            val password = editTextPassword.text.toString()

            if (user.isNotEmpty() && password.isNotEmpty()) {
                login(user, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(user: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = carl_LoginRequest(user, password)
                val response = carl_RetrofitClient.apiService.login(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()

                        // DEBUGGING: Ver qué responseCode viene
                        Toast.makeText(this@carl_LoginActivity,
                            "ResponseCode: ${apiResponse?.responseCode}",
                            Toast.LENGTH_LONG).show()

                        if (apiResponse != null &&
                            (apiResponse.responseCode == "SUCESSFUL" ||
                                    apiResponse.responseCode == "INFO_FOUND")) {

                            Toast.makeText(this@carl_LoginActivity,
                                apiResponse.message,
                                Toast.LENGTH_SHORT).show()

                            // Guardar el usuario
                            val sharedPref = getSharedPreferences("carl_prefs", MODE_PRIVATE)
                            sharedPref.edit().putString("current_user", user).apply()

                            // Ir a la lista de salas
                            val intent = Intent(this@carl_LoginActivity, carl_RoomListActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@carl_LoginActivity,
                                "Login failed. Code: ${apiResponse?.responseCode}",
                                Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@carl_LoginActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@carl_LoginActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}