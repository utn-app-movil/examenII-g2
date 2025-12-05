package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.esteb_RoomAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import Service.esteb_APIService

class esteb_ListRoomActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: esteb_RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_esteb_list_room)

        rv = findViewById(R.id.rvMedications)
        tvEmpty = findViewById(R.id.tvEmpty)

        rv.layoutManager = LinearLayoutManager(this)
        adapter = esteb_RoomAdapter(listOf())
        rv.adapter = adapter

        findViewById<ImageButton>(R.id.home_button).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btnRefresh)?.setOnClickListener {
            loadRooms()
        }

        loadRooms()
    }

    private fun loadRooms() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = esteb_APIService.api.getRooms()

                runOnUiThread {
                    val rooms = response.data

                    if (rooms.isEmpty()) {
                        tvEmpty.visibility = View.VISIBLE
                    } else {
                        tvEmpty.visibility = View.GONE
                    }
                    adapter.updateData(rooms)
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@esteb_ListRoomActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}