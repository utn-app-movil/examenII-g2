package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import util.util
import cr.ac.utn.appmovil.rooms.service.jor_APIService


class jor_AuthenticationActivity : AppCompatActivity() {

    companion object {
        var CURRENT_USER_EMAIL: String = ""
    }

    private lateinit var jor_et_user: EditText
    private lateinit var jor_et_password: EditText
    private lateinit var jor_btn_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jor_authentication)

        jor_et_user = findViewById(R.id.jor_et_user)
        jor_et_password = findViewById(R.id.jor_et_password)
        jor_btn_login = findViewById(R.id.jor_btn_login)

        jor_btn_login.setOnClickListener {
            val username = jor_et_user.text.toString().trim()
            val password = jor_et_password.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            jor_APIService.instance.getUsers().enqueue(object : retrofit2.Callback<Any> {
                override fun onResponse(call: retrofit2.Call<Any>, response: retrofit2.Response<Any>) {
                    val body = response.body() as? Map<String, Any>
                    val code = body?.get("responseCode") as? String

                    if (code == "SUCESSFUL" || code == "INFO_FOUND") {
                        val data = body["data"] as? List<Map<String, Any>>
                        var userFound = false
                        var userEmail = ""


                        data?.forEach { userMap ->
                            val userName = userMap["username"] as? String ?: ""
                            val userPass = userMap["password"] as? String ?: ""

                            if (userName == username && userPass == password) {
                                userFound = true
                            }
                        }

                        if (userFound) {
                            CURRENT_USER_EMAIL = userEmail
                            Toast.makeText(this@jor_AuthenticationActivity, "Bienvenido, $username", Toast.LENGTH_LONG).show()
                            util.openActivity(this@jor_AuthenticationActivity, jor_RoomListActivity::class.java)
                            finish()
                        } else {

                            val msg = "The user/password does not match with the right credentials or username is inactive."
                            Toast.makeText(this@jor_AuthenticationActivity, msg, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@jor_AuthenticationActivity, "Error cargando usuarios", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<Any>, t: Throwable) {
                    Toast.makeText(this@jor_AuthenticationActivity, "Sin conexión a internet", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}