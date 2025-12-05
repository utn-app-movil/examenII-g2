package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import model.wils_AuthRequest
import Service.wils_APIService

class wils_AuthenticationActivity : wils_BaseActivity() {
    
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private val apiService = wils_APIService()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wils_authentication)
        
        initViews()
        setupClickListeners()
    }
    
    private fun initViews() {
        etUsername = findViewById(R.id.wils_etUsername)
        etPassword = findViewById(R.id.wils_etPassword)
        btnLogin = findViewById(R.id.wils_btnLogin)
        tvMessage = findViewById(R.id.wils_tvMessage)
    }
    
    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                showMessage(getString(R.string.wils_fill_all_fields))
            }
        }
    }
    
    private fun authenticateUser(username: String, password: String) {
        btnLogin.isEnabled = false
        showMessage(getString(R.string.wils_authenticating))
        
        lifecycleScope.launch {
            try {
                val authRequest = wils_AuthRequest(username, password)
                val response = apiService.authenticateUser(authRequest)
                
                if (response.responseCode == "INFO_FOUND" || response.responseCode == "SUCESSFUL" || response.message.contains("successfully")) {
                    showMessage(getString(R.string.wils_login_successful))
                    kotlinx.coroutines.delay(1500)
                    navigateToRoomList(username)
                } else {
                    showMessage(getString(R.string.wils_authentication_failed) + ": ${response.message}")
                }
            } catch (e: Exception) {
                showMessage("Error de autenticación: ${e.message}")
            } finally {
                btnLogin.isEnabled = true
            }
        }
    }
    
    private fun navigateToRoomList(username: String) {
        try {
            showMessage("Navegando a lista de salas...")
            val intent = Intent(this, wils_RoomListActivity::class.java)
            intent.putExtra("USERNAME", username)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            showMessage("Error navegando: ${e.message}")
        }
    }
}