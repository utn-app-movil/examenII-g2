package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.emur_RoomAdapter
import adapter.emur_RoomActionListener
import Service.emur_APIService
import kotlinx.coroutines.launch
import model.emur_BookingRequest
import model.emur_Room
import model.emur_UnbookingRequest
import util.util
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.emur_RoomActivity
import android.content.Context

class emur_RoomListActivity : AppCompatActivity(), emur_RoomActionListener {

    private lateinit var emur_recyclerView: RecyclerView
    private lateinit var emur_refreshButton: Button
    private lateinit var emur_createButton: Button
    private lateinit var emur_adapter: emur_RoomAdapter

    private lateinit var currentUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emur_room_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emur_recyclerView = findViewById(R.id.emur_recyclerView)
        emur_refreshButton = findViewById(R.id.emur_refreshButton)
        emur_createButton = findViewById(R.id.emur_createButton)

        // 🚨 IMPLEMENTACIÓN DE SHAREDPREFERENCES (READ) 🚨
        val sharedPref = getSharedPreferences("emur_UserPrefs", Context.MODE_PRIVATE)
        // Lee el nombre de usuario o usa "GUEST" por defecto
        currentUsername = sharedPref.getString("current_username", "GUEST") ?: "GUEST"

        // Se inicializa el adaptador con el nombre de usuario leído (no hardcodeado)
        emur_adapter = emur_RoomAdapter(emptyList(), this, currentUsername)
        emur_recyclerView.layoutManager = LinearLayoutManager(this)
        emur_recyclerView.adapter = emur_adapter

        emur_refreshButton.setOnClickListener {
            emur_loadRooms()
        }
        emur_createButton.setOnClickListener {
            util.openActivity(this, emur_RoomActivity::class.java)
        }

        emur_loadRooms()
    }

    override fun onResume() {
        super.onResume()
        emur_loadRooms()
    }

    private fun emur_loadRooms() {
        lifecycleScope.launch {
            try {
                val response = emur_APIService.emur_apiRooms.getRooms()

                if (response.isSuccessful) {
                    val baseResponse = response.body()
                    val rooms = baseResponse?.data

                    if (rooms != null) {
                        emur_adapter.updateRooms(rooms)
                    } else {
                        util.showDialogCondition(this@emur_RoomListActivity,
                            getString(R.string.emur_api_error_title),
                            baseResponse?.message ?: "No se encontraron salas.",
                            "OK", "Cancelar", {}, {})
                    }
                } else {
                    util.showDialogCondition(this@emur_RoomListActivity,
                        getString(R.string.emur_api_error_title),
                        "Error HTTP al cargar salas: Código ${response.code()}.",
                        "OK", "Cancelar", {}, {})
                }
            } catch (e: Exception) {
                util.showDialogCondition(this@emur_RoomListActivity,
                    getString(R.string.emur_api_error_title),
                    "Error de red: ${e.localizedMessage}",
                    "OK", "Cancelar", {}, {})
            }
        }
    }

    private fun emur_performBooking(room: emur_Room) {
        lifecycleScope.launch {
            try {
                val bookingRequest = emur_BookingRequest(room.room, currentUsername)
                val response = emur_APIService.emur_apiRooms.bookRoom(bookingRequest)

                if (response.isSuccessful && response.body()?.responseCode?.startsWith("SU") == true) {
                    util.showDialogCondition(this@emur_RoomListActivity,
                        "Reserva Exitosa", "La sala ${room.room} ha sido reservada.",
                        "OK", "Cancelar", { emur_loadRooms() }, {})
                } else {
                    util.showDialogCondition(this@emur_RoomListActivity,
                        getString(R.string.emur_api_error_title),
                        response.body()?.message ?: "Error al reservar la sala.",
                        "OK", "Cancelar", {}, {})
                }
            } catch (e: Exception) {
                util.showDialogCondition(this@emur_RoomListActivity,
                    getString(R.string.emur_api_error_title),
                    "Error de red/servidor: ${e.localizedMessage}",
                    "OK", "Cancelar", {}, {})
            }
        }
    }

    private fun emur_performUnbooking(room: emur_Room) {
        lifecycleScope.launch {
            try {
                val unbookingRequest = emur_UnbookingRequest(room.room)
                val response = emur_APIService.emur_apiRooms.unbookRoom(unbookingRequest)

                if (response.isSuccessful && response.body()?.responseCode?.startsWith("SU") == true) { // Condición de éxito
                    util.showDialogCondition(this@emur_RoomListActivity,
                        "Liberación Exitosa", "La sala ${room.room} ha sido liberada.",
                        "OK", "Cancelar", { emur_loadRooms() }, {})
                } else {
                    util.showDialogCondition(this@emur_RoomListActivity,
                        getString(R.string.emur_api_error_title),
                        response.body()?.message ?: "Error al liberar la sala.",
                        "OK", "Cancelar", {}, {})
                }
            } catch (e: Exception) {
                util.showDialogCondition(this@emur_RoomListActivity,
                    getString(R.string.emur_api_error_title),
                    "Error de red/servidor: ${e.localizedMessage}",
                    "OK", "Cancelar", {}, {})
            }
        }
    }

    override fun onReserveClicked(room: emur_Room) {
        util.showDialogCondition(this,
            "Confirmar Reserva",
            getString(R.string.emur_reserve_room_question, room.room),
            getString(R.string.emur_positive_button),
            getString(R.string.emur_negative_button),
            { emur_performBooking(room) },
            {}
        )
    }

    override fun onUnbookClicked(room: emur_Room) {
        util.showDialogCondition(this,
            "Confirmar Liberación",
            getString(R.string.emur_release_room_question, room.room),
            getString(R.string.emur_positive_button),
            getString(R.string.emur_negative_button),
            { emur_performUnbooking(room) },
            {}
        )
    }
}