package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class alex_CreateRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alex_create_room)

        val etName = findViewById<EditText>(R.id.alex_et_create_name)
        val etCap = findViewById<EditText>(R.id.alex_et_create_capacity)
        val btnSave = findViewById<Button>(R.id.alex_btn_create_save)
        val btnBack = findViewById<Button>(R.id.alex_btn_back_create)

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val capStr = etCap.text.toString()

            if (name.isNotEmpty() && capStr.isNotEmpty()) {
                createRoom(name, capStr.toInt())
            } else {
                Toast.makeText(this, getString(R.string.alex_msg_fill_data), Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun createRoom(name: String, capacity: Int) {
        val req = alex_CreateRoomRequest(name, capacity)

        alex_APIService.api.createRoom(req).enqueue(object : Callback<alex_GenericResponse> {
            override fun onResponse(call: Call<alex_GenericResponse>, response: Response<alex_GenericResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val resp = response.body()!!
                    Toast.makeText(this@alex_CreateRoomActivity, resp.message, Toast.LENGTH_SHORT).show()

                    if (resp.responseCode == "SUCESSFUL") {
                        finish()
                    }
                } else {
                    Toast.makeText(this@alex_CreateRoomActivity, getString(R.string.alex_msg_create_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<alex_GenericResponse>, t: Throwable) {
                val errorMsg = getString(R.string.alex_msg_error_detail, t.message)
                Toast.makeText(this@alex_CreateRoomActivity, errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }
}