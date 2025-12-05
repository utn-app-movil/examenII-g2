package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.ken_room
import service.ken_ApiClient

class ken_CreateRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ken_create_room)

        val txtName = findViewById<EditText>(R.id.ken_txtRoomName)
        val txtCapacity = findViewById<EditText>(R.id.ken_txtRoomCapacity)
        val btnSave = findViewById<Button>(R.id.ken_btnSaveRoom)

        btnSave.setOnClickListener {

            // Crear la sala de acuerdo al API
            val room = ken_room(
                room = txtName.text.toString(),
                capacity = txtCapacity.text.toString().toInt(),
                isBusy = false,
                user = null,
                date = null
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = ken_ApiClient.api.createRoom(room)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ken_CreateRoomActivity,
                            "Sala creada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ken_CreateRoomActivity,
                            "Error al crear sala: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
