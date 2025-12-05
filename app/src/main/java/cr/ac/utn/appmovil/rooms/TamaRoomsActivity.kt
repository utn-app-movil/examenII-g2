package cr.ac.utn.appmovil.rooms

import adapter.tama_RoomsAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import model.tama_Room
import model.tama_BookingRequest
import tamanetwork.TamaApiProvider
import cr.ac.utn.appmovil.rooms.R

class TamaRoomsActivity : AppCompatActivity() {

    private lateinit var tama_rvRooms: RecyclerView
    private lateinit var tama_btnRefresh: Button
    private lateinit var tama_tvRoomsMessage: TextView

    private lateinit var tama_adapter: tama_RoomsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tama_rooms)

        tama_rvRooms = findViewById(R.id.tama_rvRooms)
        tama_btnRefresh = findViewById(R.id.tama_btnRefresh)
        tama_tvRoomsMessage = findViewById(R.id.tama_tvRoomsMessage)

        tama_adapter = tama_RoomsAdapter(emptyList()) { room ->
            tama_handleRoomClick(room)
        }
        tama_rvRooms.layoutManager = LinearLayoutManager(this)
        tama_rvRooms.adapter = tama_adapter

        tama_btnRefresh.setOnClickListener {
            tama_loadRooms()
        }

        tama_loadRooms()
    }

    private fun tama_loadRooms() {
        lifecycleScope.launch {
            try {
                val tama_response = TamaApiProvider.tama_api.tama_getRooms()

                if (tama_response.isSuccessful) {
                    val tama_body = tama_response.body()

                    if (tama_body != null) {
                        // mensaje del backend
                        tama_tvRoomsMessage.text =
                            "Code: ${tama_body.responseCode}\n${tama_body.message}"

                        val tama_lista: List<tama_Room> =
                            tama_body.data ?: emptyList<tama_Room>()

                        tama_adapter.tama_updateRooms(tama_lista)
                    } else {
                        tama_tvRoomsMessage.text = "Respuesta vacía"
                    }
                } else {
                    tama_tvRoomsMessage.text = "Error HTTP: ${tama_response.code()}"
                }
            } catch (e: Exception) {
                tama_tvRoomsMessage.text = "Error de conexión: ${e.localizedMessage}"
            }
        }
    }

    private fun tama_handleRoomClick(room: tama_Room) {
        lifecycleScope.launch {
            try {
                val req = tama_BookingRequest(
                    roomId = room.id,
                    username = "estudiante"
                )

                val resp = if (room.reserved) {
                    TamaApiProvider.tama_api.tama_unbookRoom(req)
                } else {
                    TamaApiProvider.tama_api.tama_bookRoom(req)
                }

                if (resp.isSuccessful) {
                    val body = resp.body()
                    tama_tvRoomsMessage.text = body?.message ?: "Sin mensaje"
                    tama_loadRooms()
                } else {
                    tama_tvRoomsMessage.text = "Error HTTP: ${resp.code()}"
                }
            } catch (e: Exception) {
                tama_tvRoomsMessage.text = "Error de conexión: ${e.localizedMessage}"
            }
        }
    }
}
