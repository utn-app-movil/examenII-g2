package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.adapter.SergRoomAdapter
import cr.ac.utn.appmovil.rooms.model.SergApiResponse
import cr.ac.utn.appmovil.rooms.model.SergBookingRequest
import cr.ac.utn.appmovil.rooms.model.SergRoom
import cr.ac.utn.appmovil.rooms.service.SergAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SergRoomsListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnRefresh: Button
    private lateinit var adapter: SergRoomAdapter
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serg_rooms_list)

        username = intent.getStringExtra("username") ?: ""

        recyclerView = findViewById(R.id.serg_recycler_rooms)
        btnRefresh = findViewById(R.id.serg_btn_refresh)

        adapter = SergRoomAdapter(
            emptyList(),
            onBookClick = { room -> confirmBooking(room) },
            onUnbookClick = { room -> confirmUnbooking(room) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnRefresh.setOnClickListener {
            loadRooms()
        }

        loadRooms()
    }

    private fun loadRooms() {
        SergAPIService.api.getAllRooms().enqueue(object : Callback<SergApiResponse<List<SergRoom>>> {
            override fun onResponse(call: Call<SergApiResponse<List<SergRoom>>>, response: Response<SergApiResponse<List<SergRoom>>>) {
                val apiResponse = response.body()
                if (apiResponse?.data != null) {
                    adapter.updateRooms(apiResponse.data)
                }
            }

            override fun onFailure(call: Call<SergApiResponse<List<SergRoom>>>, t: Throwable) {
                Toast.makeText(this@SergRoomsListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun confirmBooking(room: SergRoom) {
        AlertDialog.Builder(this)
            .setMessage("Book room ${room.room}?")
            .setPositiveButton("Yes") { _, _ -> bookRoom(room) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun bookRoom(room: SergRoom) {
        val request = SergBookingRequest(room.room, username)
        SergAPIService.api.bookRoom(request).enqueue(object : Callback<SergApiResponse<SergRoom>> {
            override fun onResponse(call: Call<SergApiResponse<SergRoom>>, response: Response<SergApiResponse<SergRoom>>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    Toast.makeText(this@SergRoomsListActivity, apiResponse.message, Toast.LENGTH_SHORT).show()
                    if (apiResponse.responseCode == "SUCESSFUL") {
                        loadRooms()
                    }
                }
            }

            override fun onFailure(call: Call<SergApiResponse<SergRoom>>, t: Throwable) {
                Toast.makeText(this@SergRoomsListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun confirmUnbooking(room: SergRoom) {
        AlertDialog.Builder(this)
            .setMessage("Release room ${room.room}?")
            .setPositiveButton("Yes") { _, _ -> unbookRoom(room) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun unbookRoom(room: SergRoom) {
        val request = SergBookingRequest(room.room)
        SergAPIService.api.unbookRoom(request).enqueue(object : Callback<SergApiResponse<SergRoom>> {
            override fun onResponse(call: Call<SergApiResponse<SergRoom>>, response: Response<SergApiResponse<SergRoom>>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    Toast.makeText(this@SergRoomsListActivity, apiResponse.message, Toast.LENGTH_SHORT).show()
                    if (apiResponse.responseCode == "SUCESSFUL") {
                        loadRooms()
                    }
                }
            }

            override fun onFailure(call: Call<SergApiResponse<SergRoom>>, t: Throwable) {
                Toast.makeText(this@SergRoomsListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}