package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import util.util


data class AuthRequest(
    val username: String,
    val password: String
)


data class UserData(
    val user: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val emailname: String? = null,
    val email: String? = null
)


data class ApiResponse<T>(
    val data: T? = null,
    val responseCode: String,
    val message: String
)



interface RoomsApi {
    @POST("/users/auth")
    suspend fun authUser(
        @Body body: AuthRequest
    ): ApiResponse<UserData?>
}



class LoginActivity : AppCompatActivity() {

    // Elementos de la UI con prefijo kmon_
    private lateinit var kmon_edtUsername: EditText
    private lateinit var kmon_edtPassword: EditText
    private lateinit var kmon_btnLogin: Button
    private lateinit var kmon_txtStatus: TextView


    private val kmon_api: RoomsApi by lazy {
        val retrofit = Retrofit.Builder()

            .baseUrl(util.apiURL + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RoomsApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val kmon_rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(60, 120, 60, 60)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }


        val kmon_txtTitle = TextView(this).apply {
            text = "Reserva de Salas"
            textSize = 24f
            gravity = Gravity.CENTER
        }
        kmon_rootLayout.addView(
            kmon_txtTitle,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 80
            }
        )


        kmon_edtUsername = EditText(this).apply {
            hint = "Usuario"
        }
        kmon_rootLayout.addView(
            kmon_edtUsername,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 40
            }
        )


        kmon_edtPassword = EditText(this).apply {
            hint = "Contraseña"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        kmon_rootLayout.addView(
            kmon_edtPassword,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 40
            }
        )


        kmon_btnLogin = Button(this).apply {
            text = "Iniciar sesión"
        }
        kmon_rootLayout.addView(
            kmon_btnLogin,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 40
            }
        )


        kmon_txtStatus = TextView(this).apply {
            text = ""
            textSize = 14f
            gravity = Gravity.CENTER
        }
        kmon_rootLayout.addView(
            kmon_txtStatus,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )


        setContentView(kmon_rootLayout)


        kmon_btnLogin.setOnClickListener {
            val username = kmon_edtUsername.text.toString().trim()
            val password = kmon_edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                kmon_txtStatus.text = "Por favor complete usuario y contraseña."
            } else {

                autenticarUsuario(username, password)
            }
        }
    }



    private fun autenticarUsuario(username: String, password: String) {

        kmon_txtStatus.text = "Autenticando..."


        CoroutineScope(Dispatchers.Main).launch {
            try {

                val response = withContext(Dispatchers.IO) {
                    kmon_api.authUser(
                        AuthRequest(
                            username = username,
                            password = password
                        )
                    )
                }


                val code = response.responseCode
                val msg = response.message

                if (code == "SUCESSFUL" || code == "INFO_FOUND") {

                    val userData = response.data
                    val emailFromData =
                        userData?.email ?: userData?.emailname ?: userData?.user
                    val identifierToUse = emailFromData ?: username


                    kmon_txtStatus.text = "✅ $msg"


                    util.openActivity(
                        this@LoginActivity,
                        RoomsActivity::class.java,
                        "kmon_username",
                        identifierToUse
                    )



                } else {


                    kmon_txtStatus.text = "⚠️ $msg"
                }

            } catch (e: Exception) {

                kmon_txtStatus.text = "❌ Error: ${e.localizedMessage}"
            }
        }
    }
}
