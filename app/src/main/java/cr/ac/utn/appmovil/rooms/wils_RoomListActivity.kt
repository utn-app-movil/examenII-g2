package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import adapter.wils_RoomAdapter
import model.wils_Room
import model.wils_BookRoomRequest
import model.wils_UnbookRoomRequest
import Service.wils_APIService

class wils_RoomListActivity : wils_BaseActivity() {
    
    private lateinit var rvRooms: RecyclerView
    private lateinit var btnRefresh: Button
    private lateinit var btnCreateRoom: Button
    private lateinit var roomAdapter: wils_RoomAdapter
    private val apiService = wils_APIService()
    private var roomsList = mutableListOf<wils_Room>()
    private var authenticatedUsername: String = ""
    private var userEmail: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wils_room_list)

        authenticatedUsername = intent.getStringExtra("USERNAME") ?: "usuario"
        
        initViews()
        setupRecyclerView()
        setupClickListeners()
        loadUserData()
        loadRooms()
    }
    
    private fun initViews() {
        rvRooms = findViewById(R.id.wils_rvRooms)
        btnRefresh = findViewById(R.id.wils_btnRefresh)
        btnCreateRoom = findViewById(R.id.wils_btnCreateRoom)
        tvMessage = findViewById(R.id.wils_tvMessage)
    }
    
    private fun setupRecyclerView() {
        val context = this
        roomAdapter = wils_RoomAdapter(roomsList) { room ->
            handleRoomClick(room)
        }
        rvRooms.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = roomAdapter
        }
    }
    
    private fun setupClickListeners() {
        btnRefresh.setOnClickListener {
            loadRooms()
        }
        
        btnCreateRoom.setOnClickListener {
            val intent = Intent(this, wils_RoomActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun handleRoomClick(room: wils_Room) {
        if (room.is_busy) {
            unbookRoom(room)
        } else {
            bookRoom(room)
        }
    }
    
    private fun loadUserData() {
        lifecycleScope.launch {
            try {
                val response = apiService.getUsers()
                if (response.responseCode == "SUCESSFUL" && response.data != null) {
                    val currentUser = response.data.find { it.username == authenticatedUsername }
                    if (currentUser != null) {
                        userEmail = currentUser.email
                    } else {
                        showMessage("Error: Usuario no encontrado en el sistema")
                    }
                } else {
                    showMessage("Error: No se pudieron obtener los datos de usuarios")
                }
            } catch (e: Exception) {
                showMessage("Error de conexión: ${e.message}")
            }
        }
    }
    
    private fun loadRooms() {
        showMessage(getString(R.string.wils_loading_rooms))
        
        lifecycleScope.launch {
            try {
                val response = apiService.getRooms()
                
                if (response.responseCode == "SUCESSFUL" && response.data != null) {
                    roomsList.clear()
                    roomsList.addAll(response.data)
                    roomAdapter.updateRooms(roomsList)
                    hideMessage()
                } else {
                    showMessage(response.message)
                }
            } catch (e: Exception) {
                showMessage("Error cargando salas: ${e.message}")
            }
        }
    }
    
    private fun bookRoom(room: wils_Room) {

        if (userEmail.isEmpty()) {
            showMessage("Error: No se ha obtenido el email del usuario. Intente actualizar.")
            return
        }
        
        val context = this
        lifecycleScope.launch {
            try {
                val bookRequest = wils_BookRoomRequest(room.room, userEmail)
                val response = apiService.bookRoom(bookRequest)
                
                if (response.responseCode == "SUCESSFUL" || response.message.contains("successfully")) {
                    Toast.makeText(context, getString(R.string.wils_room_booked_success), Toast.LENGTH_SHORT).show()
                    kotlinx.coroutines.delay(500)
                    loadRooms()
                } else {
                    showMessage(response.message)
                }
            } catch (e: Exception) {
                showMessage("Error reservando sala: ${e.message}")
            }
        }
    }
    
    private fun unbookRoom(room: wils_Room) {
        val context = this
        lifecycleScope.launch {
            try {
                val unbookRequest = wils_UnbookRoomRequest(room.room)
                val response = apiService.unbookRoom(unbookRequest)
                
                if (response.responseCode == "SUCESSFUL" || response.message.contains("successfully")) {
                    Toast.makeText(context, getString(R.string.wils_room_released_success), Toast.LENGTH_SHORT).show()
                    kotlinx.coroutines.delay(500)
                    loadRooms()
                } else {
                    showMessage(response.message)
                }
            } catch (e: Exception) {
                showMessage("Error cancelando reserva: ${e.message}")
            }
        }
    }
    
    private fun hideMessage() {
        tvMessage.visibility = View.GONE
    }
}