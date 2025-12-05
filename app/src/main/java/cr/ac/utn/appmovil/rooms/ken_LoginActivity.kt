package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.app.ken_RoomListActivity
import kotlinx.coroutines.*
import service.ken_ApiClient

class ken_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ken_login)

        val txtUser = findViewById<EditText>(R.id.ken_txtUsername)
        val txtPass = findViewById<EditText>(R.id.ken_txtPassword)
        val btnLogin = findViewById<Button>(R.id.ken_btnLogin)

        btnLogin.setOnClickListener {
            val user = txtUser.text.toString()
            val pass = txtPass.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ken_ApiClient.api.login(
                        mapOf("email" to user, "password" to pass)
                    )

                    withContext(Dispatchers.Main) {
                        if (response.responseCode == 200) {
                            Toast.makeText(
                                this@ken_LoginActivity,
                                "Bienvenido",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this@ken_LoginActivity, ken_RoomListActivity::class.java))
                        } else {
                            Toast.makeText(
                                this@ken_LoginActivity,
                                "Credenciales incorrectas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ken_LoginActivity,
                            "Error de conexión",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
