package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import Service.emur_APIService
import kotlinx.coroutines.launch
import model.emur_AuthRequest
import util.util
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.emur_RoomListActivity

class emur_AuthenticationActivity : AppCompatActivity() {


    private lateinit var emur_usernameEditText: EditText
    private lateinit var emur_passwordEditText: EditText
    private lateinit var emur_loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emur_authentication)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emur_usernameEditText = findViewById(R.id.emur_usernameEditText)
        emur_passwordEditText = findViewById(R.id.emur_passwordEditText)
        emur_loginButton = findViewById(R.id.emur_loginButton)

        emur_loginButton.setOnClickListener {
            emur_authenticateUser()
        }
    }


    private fun emur_authenticateUser() {

        val inputUsername = emur_usernameEditText.text.toString().trim()
        val inputPassword = emur_passwordEditText.text.toString().trim()

        if (inputUsername.isBlank() || inputPassword.isBlank()) {
            util.showDialogCondition(
                this,
                getString(R.string.emur_api_error_title),
                "El usuario y la contraseña no pueden estar vacíos.",
                "OK", "Cancelar", {}, {}
            )
            return
        }

        lifecycleScope.launch {
            try {
                val authRequest = emur_AuthRequest(inputUsername, inputPassword)
                val response = emur_APIService.emur_apiRooms.authenticateUser(authRequest)

                if (response.isSuccessful) {
                    val baseResponse = response.body()

                    val code = baseResponse?.responseCode?.trim()?.uppercase() ?: ""
                    val message = baseResponse?.message?.trim()?.uppercase() ?: ""

                    val isSuccess = code.startsWith("SU") || message.contains("SUCCES") || message.contains("EXECUTED")

                    if (isSuccess) {

                        val realUsername = "ermurilloba"
                        val emailDomain = "@est.utn.ac.cr"
                        val userEmail = realUsername + emailDomain

                        val sharedPref = getSharedPreferences("emur_UserPrefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("current_username", userEmail)
                            apply()
                        }

                        util.showDialogCondition(
                            this@emur_AuthenticationActivity,
                            "¡Bienvenido!",
                            "Logueo exitoso. Usuario: $userEmail",
                            "OK", "Cancelar",
                            {
                                util.openActivity(this@emur_AuthenticationActivity, emur_RoomListActivity::class.java)
                                finish()
                            },
                            {}
                        )

                    } else {
                        util.showDialogCondition(
                            this@emur_AuthenticationActivity,
                            getString(R.string.emur_api_error_title),
                            baseResponse?.message ?: "Error desconocido en el logueo.",
                            "OK", "Cancelar", {}, {}
                        )
                    }

                } else {
                    val errorCode = response.code()
                    val errorMessage = when (errorCode) {
                        400 -> "Error 400: Solicitud Inválida. Verifique los datos."
                        401 -> "Error 401: Credenciales incorrectas."
                        403 -> "Error 403: Acceso denegado."
                        500 -> "Error 500: Fallo interno del servidor."
                        else -> "Error HTTP $errorCode: Fallo en la conexión con la API."
                    }

                    util.showDialogCondition(
                        this@emur_AuthenticationActivity,
                        getString(R.string.emur_api_error_title),
                        errorMessage,
                        "OK", "Cancelar", {}, {}
                    )
                }

            } catch (e: Exception) {
                util.showDialogCondition(
                    this@emur_AuthenticationActivity,
                    getString(R.string.emur_api_error_title),
                    "Error de red/servidor: ${e.localizedMessage}",
                    "OK", "Cancelar", {}, {}
                )
            }
        }
    }
}