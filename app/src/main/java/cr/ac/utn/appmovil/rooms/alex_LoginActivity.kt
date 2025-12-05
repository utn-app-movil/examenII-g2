package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class alex_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alex_login)

        val etUser = findViewById<EditText>(R.id.alex_et_username)
        val etPass = findViewById<EditText>(R.id.alex_et_password)
        val btnLogin = findViewById<Button>(R.id.alex_btn_login)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                doLogin(user, pass)
            } else {
                Toast.makeText(this, getString(R.string.alex_msg_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun doLogin(user: String, pass: String) {
        val request = alex_AuthRequest(user, pass)

        alex_APIService.api.authUser(request).enqueue(object : Callback<alex_AuthResponse> {
            override fun onResponse(call: Call<alex_AuthResponse>, response: Response<alex_AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val resp = response.body()!!

                    if (resp.responseCode == "INFO_FOUND") {
                        Toast.makeText(this@alex_LoginActivity, resp.message, Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@alex_LoginActivity, alex_RoomListActivity::class.java)
                        intent.putExtra("CURRENT_USER", resp.data?.user)
                        intent.putExtra("CURRENT_EMAIL", resp.data?.email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@alex_LoginActivity, resp.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@alex_LoginActivity, getString(R.string.alex_msg_auth_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<alex_AuthResponse>, t: Throwable) {
                val errorMsg = getString(R.string.alex_msg_net_error, t.message)
                Toast.makeText(this@alex_LoginActivity, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }
}