package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import model.mich_RoomCreateRequest
import model.mich_RoomResponse
import Service.mich_APIService
import util.util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class mich_roomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mich_room)

        val etRoomName = findViewById<EditText>(R.id.etRoomName)
        val etCapacity = findViewById<EditText>(R.id.etCapacity)
        val btnCreate = findViewById<Button>(R.id.btnCreateRoom)

        btnCreate.setOnClickListener {

            val roomName = etRoomName.text.toString().trim()
            val capacityText = etCapacity.text.toString().trim()

            if (roomName.isEmpty() || capacityText.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val capacity = capacityText.toInt()

            val request = mich_RoomCreateRequest(roomName, capacity)

            mich_APIService.api.mich_createRoom(request)
                .enqueue(object : Callback<mich_RoomResponse> {
                    override fun onResponse(
                        call: Call<mich_RoomResponse>,
                        response: Response<mich_RoomResponse>
                    ) {
                        val body = response.body()

                        if (response.isSuccessful && body != null) {
                            if (body.responseCode == "SUCESSFUL") {
                                Toast.makeText(
                                    this@mich_roomActivity,
                                    "Sala creada correctamente",
                                    Toast.LENGTH_LONG
                                ).show()
                                util.openActivity(
                                    this@mich_roomActivity,
                                    mich_mainActivity::class.java
                                )
                            } else {
                                Toast.makeText(
                                    this@mich_roomActivity,
                                    body.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@mich_roomActivity,
                                "Error al crear sala",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<mich_RoomResponse>, t: Throwable) {
                        Toast.makeText(
                            this@mich_roomActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
