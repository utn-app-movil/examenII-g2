package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.ken_roomAdapter
import cr.ac.utn.appmovil.rooms.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.ken_room
import service.ken_ApiClient

class ken_RoomListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ken_roomAdapter
    private val rooms = mutableListOf<ken_room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ken_room_list)

        recycler = findViewById(R.id.ken_rvRooms)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = ken_roomAdapter(rooms)
        recycler.adapter = adapter

        val btnRefresh = findViewById<Button>(R.id.ken_btnRefresh)

        btnRefresh.setOnClickListener { loadRooms() }

        loadRooms()
    }

    private fun loadRooms() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val result = ken_ApiClient.api.getRooms()

                withContext(Dispatchers.Main) {

                    rooms.clear()
                    rooms.addAll(result.data ?: emptyList())
                    adapter.notifyDataSetChanged()

                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ken_RoomListActivity,
                        "Error cargando salas: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
