package cr.ac.utn.appmovil.rooms

import Service.cagui_APIService
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.cagui_AuthRequest
import util.util

class cagui_authentication : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cagui_activity_authentication)

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        etUsername = findViewById(R.id.cagui_etUsername)
        etPassword = findViewById(R.id.cagui_etPassword)
        btnLogin = findViewById(R.id.cagui_btnLogin)
        progressBar = findViewById(R.id.cagui_progressBar)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.cagui_fill_fields), Toast.LENGTH_SHORT).show()
            return
        }

        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authRequest = cagui_AuthRequest(username, password)
                val response = cagui_APIService.api.authenticate(authRequest)

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    handleAuthResponse(response.responseCode, response.message)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(
                        this@cagui_authentication,
                        getString(R.string.cagui_error_network),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleAuthResponse(responseCode: String, message: String) {
        when (responseCode) {
            "SUCESSFUL", "INFO_FOUND" -> {
                Toast.makeText(this, getString(R.string.cagui_auth_success), Toast.LENGTH_SHORT).show()

                val sharedPref = getSharedPreferences("cagui_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("username", etUsername.text.toString())
                    apply()
                }

                util.openActivity(this, cagui_roomList::class.java)
                finish()
            }
            else -> {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLogin.isEnabled = !isLoading
        etUsername.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
    }
}
