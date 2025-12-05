package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.model.SergApiResponse
import cr.ac.utn.appmovil.rooms.model.SergAuthRequest
import cr.ac.utn.appmovil.rooms.model.SergUser
import cr.ac.utn.appmovil.rooms.service.SergAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.util

class SergLoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnViewUsers: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serg_login)

        etUsername = findViewById(R.id.serg_et_username)
        etPassword = findViewById(R.id.serg_et_password)
        btnLogin = findViewById(R.id.serg_btn_login)
        btnViewUsers = findViewById(R.id.serg_btn_view_users)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authenticateUser(username, password)
        }

        btnViewUsers.setOnClickListener {
            loadUsers()
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val request = SergAuthRequest(username, password)
        SergAPIService.api.authenticateUser(request).enqueue(object : Callback<SergApiResponse<SergUser>> {
            override fun onResponse(call: Call<SergApiResponse<SergUser>>, response: Response<SergApiResponse<SergUser>>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    Toast.makeText(this@SergLoginActivity, apiResponse.message, Toast.LENGTH_SHORT).show()
                    if (apiResponse.responseCode == "SUCESSFUL" || apiResponse.responseCode == "INFO_FOUND") {
                        util.openActivity(this@SergLoginActivity, SergCreateRoomActivity::class.java, "username", username)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<SergApiResponse<SergUser>>, t: Throwable) {
                Toast.makeText(this@SergLoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadUsers() {
        SergAPIService.api.getAllUsers().enqueue(object : Callback<SergApiResponse<List<SergUser>>> {
            override fun onResponse(call: Call<SergApiResponse<List<SergUser>>>, response: Response<SergApiResponse<List<SergUser>>>) {
                val apiResponse = response.body()
                if (apiResponse?.data != null) {
                    val users = apiResponse.data.filter { it.isActive }
                    val userList = users.map { "${it.username} - ${it.name} ${it.lastname}" }
                    AlertDialog.Builder(this@SergLoginActivity)
                        .setTitle("Active Users")
                        .setItems(userList.toTypedArray(), null)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }

            override fun onFailure(call: Call<SergApiResponse<List<SergUser>>>, t: Throwable) {
                Toast.makeText(this@SergLoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}