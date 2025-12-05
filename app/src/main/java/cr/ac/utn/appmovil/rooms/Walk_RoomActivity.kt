package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Walk_RoomActivity : AppCompatActivity() {

    private lateinit var roomNameInput: EditText
    private lateinit var capacityInput: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.walk_activity_room)

        roomNameInput = findViewById(R.id.walk_room_name)
        capacityInput = findViewById(R.id.walk_room_capacity)
        createButton = findViewById(R.id.walk_room_create_button)

        createButton.setOnClickListener {
            createAndReserveRoom()
        }
    }

    private fun createAndReserveRoom() {
        val name = roomNameInput.text.toString().trim()
        val capacity = capacityInput.text.toString().toIntOrNull()

        if (name.isEmpty() || capacity == null || capacity <= 0) {
            Toast.makeText(this, "Complete all fields with valid numbers", Toast.LENGTH_SHORT).show()
            return
        }

        val createBody = mapOf(
            "room" to name,
            "capacity" to capacity
        )

        // 1️⃣ Crear la sala
        Walk_ApiClient.roomService.createRoom(createBody)
            .enqueue(object : Callback<Walk_ApiResponse<Any>> {
                override fun onResponse(
                    call: Call<Walk_ApiResponse<Any>>,
                    response: Response<Walk_ApiResponse<Any>>
                ) {
                    Toast.makeText(
                        this@Walk_RoomActivity,
                        response.body()?.message ?: "Room created",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 2️⃣ Reservar la sala automáticamente con el email del usuario
                    val email = Walk_Session.currentUser?.email ?: run {
                        Toast.makeText(
                            this@Walk_RoomActivity,
                            "User email not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val bookBody = mapOf(
                        "room" to name,
                        "username" to email
                    )

                    Walk_ApiClient.roomService.bookRoom(bookBody)
                        .enqueue(object : Callback<Walk_ApiResponse<Any>> {
                            override fun onResponse(
                                call: Call<Walk_ApiResponse<Any>>,
                                response: Response<Walk_ApiResponse<Any>>
                            ) {
                                Toast.makeText(
                                    this@Walk_RoomActivity,
                                    "Room reserved for ${email}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Volver a lista de salas
                                finish()
                            }

                            override fun onFailure(call: Call<Walk_ApiResponse<Any>>, t: Throwable) {
                                Toast.makeText(
                                    this@Walk_RoomActivity,
                                    "Room created but failed to reserve",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        })
                }

                override fun onFailure(call: Call<Walk_ApiResponse<Any>>, t: Throwable) {
                    Toast.makeText(this@Walk_RoomActivity, "Error creating room", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
