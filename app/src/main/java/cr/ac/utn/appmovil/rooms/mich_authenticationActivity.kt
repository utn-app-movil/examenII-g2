package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import model.mich_AuthRequest
import model.mich_AuthResponse
import Service.mich_APIService
import util.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class mich_authenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mich_authentication)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username = etUser.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = mich_AuthRequest(username, password)

            mich_APIService.api.mich_authUser(request).enqueue(object : Callback<mich_AuthResponse> {
                override fun onResponse(
                    call: Call<mich_AuthResponse>,
                    response: Response<mich_AuthResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val body = response.body()!!

                        if (body.responseCode == "INFO_FOUND") {

                            Toast.makeText(
                                this@mich_authenticationActivity,
                                "Bienvenido ${body.data?.name}",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Ir al menú principal mich_
                            util.openActivity(
                                this@mich_authenticationActivity,
                                mich_mainActivity::class.java,
                                "username",
                                username
                            )

                        } else {
                            Toast.makeText(
                                this@mich_authenticationActivity,
                                body.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@mich_authenticationActivity,
                            "Error en autenticación",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<mich_AuthResponse>, t: Throwable) {
                    Toast.makeText(
                        this@mich_authenticationActivity,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
