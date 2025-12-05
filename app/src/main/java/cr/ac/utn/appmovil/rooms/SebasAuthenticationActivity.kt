package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sebas_controller.UserController
import util.util

class SebasAuthenticationActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvMessage: TextView
    private lateinit var pbLogin: ProgressBar
    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sebas_activity_authentication)

        etUsername = findViewById(R.id.sebas_et_username)
        etPassword = findViewById(R.id.sebas_et_password)
        btnLogin = findViewById(R.id.sebas_btn_login)
        tvMessage = findViewById(R.id.sebas_tv_login_message)
        pbLogin = findViewById(R.id.sebas_pb_login)
        userController = UserController(this@SebasAuthenticationActivity)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.sebas_error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            pbLogin.visibility = ProgressBar.VISIBLE
            btnLogin.isEnabled = false

            lifecycleScope.launch {
                try {

                    val authRequest = SebasAuthRequest(username, password)
                    val response = userController.authenticateUser(authRequest)




                    pbLogin.visibility = ProgressBar.GONE
                    btnLogin.isEnabled = true
                    
                    if (response.responseCode == "INFO_FOUND" || response.responseCode == "SUCESSFUL") {
                        Toast.makeText(this@SebasAuthenticationActivity, response.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SebasAuthenticationActivity, SebasRoomListActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                        finish()
                    } else {
                        tvMessage.text = response.message
                        tvMessage.visibility = TextView.VISIBLE
                    }
                } catch (e: Exception) {
                    pbLogin.visibility = ProgressBar.GONE
                    btnLogin.isEnabled = true
                    Toast.makeText(this@SebasAuthenticationActivity, getString(R.string.sebas_error_network), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
