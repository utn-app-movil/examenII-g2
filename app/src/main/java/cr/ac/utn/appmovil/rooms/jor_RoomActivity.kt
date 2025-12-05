package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.service.jor_APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class jor_RoomActivity : AppCompatActivity() {

    private lateinit var jor_et_room_name: EditText
    private lateinit var jor_et_capacity: EditText
    private lateinit var jor_btn_create: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jor_room)

        jor_et_room_name = findViewById(R.id.jor_et_room_name)
        jor_et_capacity = findViewById(R.id.jor_et_capacity)
        jor_btn_create = findViewById(R.id.jor_btn_create)

        jor_btn_create.setOnClickListener {
            val roomName = jor_et_room_name.text.toString().trim()
            val capacityText = jor_et_capacity.text.toString().trim()
            val capacity = capacityText.toIntOrNull() ?: 0

            if (roomName.isEmpty() || capacity <= 0) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = mapOf("room" to roomName, "capacity" to capacity)

            jor_APIService.instance.createRoom(body).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val msg = (response.body() as? Map<String, Any>)?.get("message") as? String
                        ?: "Sala creada"
                    Toast.makeText(this@jor_RoomActivity, msg, Toast.LENGTH_LONG).show()
                    finish()
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(this@jor_RoomActivity, "Sin conexión", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}