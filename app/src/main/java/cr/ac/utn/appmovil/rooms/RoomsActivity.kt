package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import util.util

data class RoomCreateRequest(
    val room: String,
    val capacity: Int
)


data class Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String,
    val date: String?
)


data class RoomApiResponse(
    val data: Any? = null,
    val responseCode: String,
    val message: String
)


data class RoomsListResponse(
    val data: List<Room>?,
    val responseCode: String,
    val message: String
)


data class RoomBookingRequest(
    val room: String,
    val username: String
)


data class RoomUnbookingRequest(
    val room: String
)


interface RoomsApiService {
    @POST("/rooms")
    suspend fun createRoom(
        @Body body: RoomCreateRequest
    ): RoomApiResponse

    @GET("/rooms")
    suspend fun getRooms(): RoomsListResponse

    @PUT("/rooms/booking")
    suspend fun bookRoom(
        @Body body: RoomBookingRequest
    ): RoomApiResponse

    @PUT("/rooms/unbooking")
    suspend fun unbookRoom(
        @Body body: RoomUnbookingRequest
    ): RoomApiResponse
}



class RoomsActivity : AppCompatActivity() {

    private lateinit var kmon_txtWelcome: TextView
    private lateinit var kmon_txtInfo: TextView


    private lateinit var kmon_edtRoomName: EditText
    private lateinit var kmon_edtCapacity: EditText
    private lateinit var kmon_btnCreateRoom: Button
    private lateinit var kmon_txtRoomStatus: TextView


    private lateinit var kmon_btnRefreshRooms: Button
    private lateinit var kmon_txtRoomsList: TextView


    private lateinit var kmon_edtSelectedRoom: EditText
    private lateinit var kmon_btnBookRoom: Button
    private lateinit var kmon_btnUnbookRoom: Button
    private lateinit var kmon_txtBookingStatus: TextView


    private var kmon_loggedUsername: String = "usuario"


    private val kmon_apiRooms: RoomsApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(util.apiURL + "/") // https://rooms-api.azurewebsites.net/
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RoomsApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        kmon_loggedUsername = intent.getStringExtra("kmon_username") ?: "usuario"



        val kmon_scrollView = ScrollView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }


        val kmon_rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(60, 120, 60, 60)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        kmon_scrollView.addView(
            kmon_rootLayout,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )





        kmon_txtWelcome = TextView(this).apply {
            text = "Bienvenido, $kmon_loggedUsername"
            textSize = 22f
            gravity = Gravity.CENTER
        }
        kmon_rootLayout.addView(
            kmon_txtWelcome,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 30
            }
        )


        kmon_txtInfo = TextView(this).apply {
            text = "Creación y gestión de salas"
            textSize = 18f
            gravity = Gravity.CENTER
        }
        kmon_rootLayout.addView(
            kmon_txtInfo,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 40
            }
        )




        kmon_edtRoomName = EditText(this).apply {
            hint = "Nombre de la sala (ej: ZA-100)"
        }
        kmon_rootLayout.addView(
            kmon_edtRoomName,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 30
            }
        )


        kmon_edtCapacity = EditText(this).apply {
            hint = "Capacidad (número)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        kmon_rootLayout.addView(
            kmon_edtCapacity,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 30
            }
        )


        kmon_btnCreateRoom = Button(this).apply {
            text = "Crear sala"
        }
        kmon_rootLayout.addView(
            kmon_btnCreateRoom,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }
        )


        kmon_txtRoomStatus = TextView(this).apply {
            text = ""
            textSize = 14f
            gravity = Gravity.CENTER
        }
        kmon_rootLayout.addView(
            kmon_txtRoomStatus,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 40
            }
        )


        kmon_btnRefreshRooms = Button(this).apply {
            text = "Refrescar lista de salas"
        }
        kmon_rootLayout.addView(
            kmon_btnRefreshRooms,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }
        )


        kmon_txtRoomsList = TextView(this).apply {
            text = "No se han cargado salas todavía."
            textSize = 14f
            gravity = Gravity.START
        }
        kmon_rootLayout.addView(
            kmon_txtRoomsList,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )


        kmon_edtSelectedRoom = EditText(this).apply {
            hint = "Nombre de la sala a seleccionar (ej: ZA-100)"
        }
        kmon_rootLayout.addView(
            kmon_edtSelectedRoom,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 40
                bottomMargin = 20
            }
        )


        kmon_btnBookRoom = Button(this).apply {
            text = "Reservar sala"
        }
        kmon_rootLayout.addView(
            kmon_btnBookRoom,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }
        )


        kmon_btnUnbookRoom = Button(this).apply {
            text = "Liberar sala"
        }
        kmon_rootLayout.addView(
            kmon_btnUnbookRoom,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 20
            }
        )


        kmon_txtBookingStatus = TextView(this).apply {
            text = ""
            textSize = 14f
            gravity = Gravity.CENTER
        }
        kmon_rootLayout.addView(
            kmon_txtBookingStatus,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )


        setContentView(kmon_scrollView)


        kmon_btnCreateRoom.setOnClickListener {
            val roomName = kmon_edtRoomName.text.toString().trim()
            val capacityText = kmon_edtCapacity.text.toString().trim()

            if (roomName.isEmpty() || capacityText.isEmpty()) {
                kmon_txtRoomStatus.text =
                    "Por favor complete el nombre de la sala y la capacidad."
                return@setOnClickListener
            }

            val capacity = capacityText.toIntOrNull()
            if (capacity == null || capacity <= 0) {
                kmon_txtRoomStatus.text = "La capacidad debe ser un número mayor que 0."
                return@setOnClickListener
            }

            crearSala(roomName, capacity)
        }


        kmon_btnRefreshRooms.setOnClickListener {
            cargarSalas()
        }


        kmon_btnBookRoom.setOnClickListener {
            val roomName = kmon_edtSelectedRoom.text.toString().trim()
            if (roomName.isEmpty()) {
                kmon_txtBookingStatus.text = "Por favor escriba el nombre de la sala."
            } else {
                reservarSala(roomName)
            }
        }


        kmon_btnUnbookRoom.setOnClickListener {
            val roomName = kmon_edtSelectedRoom.text.toString().trim()
            if (roomName.isEmpty()) {
                kmon_txtBookingStatus.text = "Por favor escriba el nombre de la sala."
            } else {
                liberarSala(roomName)
            }
        }
    }



    private fun crearSala(roomName: String, capacity: Int) {
        kmon_txtRoomStatus.text = "Creando sala..."

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    kmon_apiRooms.createRoom(
                        RoomCreateRequest(
                            room = roomName,
                            capacity = capacity
                        )
                    )
                }

                val code = response.responseCode
                val msg = response.message

                if (code == "SUCESSFUL" || code == "INFO_FOUND") {
                    // Creación exitosa
                    kmon_txtRoomStatus.text = "✅ $msg"
                    kmon_edtRoomName.text.clear()
                    kmon_edtCapacity.text.clear()
                } else {
                    kmon_txtRoomStatus.text = "⚠️ $msg"
                }

            } catch (e: Exception) {
                kmon_txtRoomStatus.text = "❌ Error: ${e.localizedMessage}"
            }
        }
    }



    private fun cargarSalas() {
        kmon_txtRoomsList.text = "Cargando salas..."

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    kmon_apiRooms.getRooms()
                }

                val code = response.responseCode
                val msg = response.message

                if (code == "SUCESSFUL" || code == "INFO_FOUND") {
                    val rooms = response.data

                    if (rooms.isNullOrEmpty()) {
                        kmon_txtRoomsList.text = "No hay salas registradas."
                    } else {
                        val builder = StringBuilder()
                        for (room in rooms) {
                            val estado = if (!room.is_busy) {
                                "DISPONIBLE"
                            } else {
                                val usuario =
                                    if (room.user.isNotBlank()) room.user else "ocupada"
                                "OCUPADA por $usuario"
                            }

                            builder.append("Sala: ${room.room}\n")
                            builder.append("Capacidad: ${room.capacity}\n")
                            builder.append("Estado: $estado\n")
                            builder.append("-------------------------\n")
                        }
                        kmon_txtRoomsList.text = builder.toString()
                    }
                } else {
                    kmon_txtRoomsList.text = "⚠️ $msg"
                }

            } catch (e: Exception) {
                kmon_txtRoomsList.text = "❌ Error: ${e.localizedMessage}"
            }
        }
    }



    private fun reservarSala(roomName: String) {
        kmon_txtBookingStatus.text = "Reservando sala..."

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    kmon_apiRooms.bookRoom(
                        RoomBookingRequest(
                            room = roomName,
                            username = kmon_loggedUsername
                        )
                    )
                }

                val code = response.responseCode
                val msg = response.message

                if (code == "SUCESSFUL" || code == "INFO_FOUND") {
                    kmon_txtBookingStatus.text = "✅ $msg"
                    // Después de reservar, podemos refrescar la lista
                    cargarSalas()
                } else {
                    kmon_txtBookingStatus.text = "⚠️ $msg"
                }

            } catch (e: Exception) {
                kmon_txtBookingStatus.text = "❌ Error: ${e.localizedMessage}"
            }
        }
    }



    private fun liberarSala(roomName: String) {
        kmon_txtBookingStatus.text = "Liberando sala..."

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    kmon_apiRooms.unbookRoom(
                        RoomUnbookingRequest(
                            room = roomName
                        )
                    )
                }

                val code = response.responseCode
                val msg = response.message

                if (code == "SUCESSFUL" || code == "INFO_FOUND") {
                    kmon_txtBookingStatus.text = "✅ $msg"
                    // Después de liberar, refrescamos la lista
                    cargarSalas()
                } else {
                    kmon_txtBookingStatus.text = "⚠️ $msg"
                }

            } catch (e: Exception) {
                kmon_txtBookingStatus.text = "❌ Error: ${e.localizedMessage}"
            }
        }
    }
}
