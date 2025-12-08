package cr.ac.utn.appmovil.rooms

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import interfaces.kpica_OnItemClickListener
import kotlinx.coroutines.launch
import kpica_Controller.kpica_RoomController
import kpica_Entity.kpica_Room
import kpica_Entity.kpica_User
import util.util

class kpica_RoomActivity : AppCompatActivity(), kpica_OnItemClickListener {
    private lateinit var txtUser: EditText
    private lateinit var btnBooking: Button
    private lateinit var btnUnBooking: Button
    private lateinit var roomSelected: kpica_Room
    private lateinit var recycler: RecyclerView
    private lateinit var roomController: kpica_RoomController
    private lateinit var user: kpica_User
    private lateinit var kpica_RoomListAdapter: kpica_RoomListAdapter
    private lateinit var mycontext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.kpica_room_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kpica_TableLayout_room)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        roomSelected = kpica_Room()
        txtUser = findViewById(R.id.kpica_txtUser)
        btnBooking = findViewById(R.id.kpica_btn_BookingRoom)
        btnUnBooking = findViewById(R.id.kpica_btn_UnBookingRoom)
        user = kpica_LoginActivity.SessionManager.user!!
        txtUser.setText("${user.Name} ${user.LastName}")
        recycler = findViewById<RecyclerView>(R.id.kpica_rvroom)

        mycontext = this
        roomController = kpica_RoomController(mycontext)

        btnBooking.setOnClickListener (View.OnClickListener{ view ->
            bookingRoom()
        })

        btnUnBooking.setOnClickListener (View.OnClickListener{ view ->
            unBookingRoom()
        })

        val btn_AddRoom = findViewById<Button>(R.id.kpica_btn_AddRoom)
        btn_AddRoom.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(mycontext, kpica_RoomAddActivity::class.java)
        })

        val btn_reloadRoom = findViewById<ImageButton>(R.id.kpica_btn_reloadRoom)
        btn_reloadRoom.setOnClickListener(View.OnClickListener { view ->
            getRoom()
            showHideButtons()
        })

        getRoom()
        showHideButtons()
    }

    private fun getRoom() {
        val context = this
        lifecycleScope.launch {
            kpica_RoomListAdapter = kpica_RoomListAdapter(roomController.getRoom(),
                context)
            val layoutManager = LinearLayoutManager(applicationContext)
            recycler.layoutManager = layoutManager
            recycler.adapter = kpica_RoomListAdapter
            kpica_RoomListAdapter.notifyDataSetChanged()
        }
    }

    private fun bookingRoom() {
        try {
            lifecycleScope.launch {
                roomSelected.User = user.Email
                roomController.updateBookingRoom(roomSelected)
            }
            roomSelected = kpica_Room()
            getRoom()
            showHideButtons()
        } catch (e: Exception) {
            Toast.makeText(mycontext, e.message,
                Toast.LENGTH_LONG).show()
        }
    }

    private fun unBookingRoom(): Unit {
        try {
            lifecycleScope.launch {
                roomController.updateUnBookingRoom(roomSelected)
            }
            roomSelected = kpica_Room()
            getRoom()
            showHideButtons()
        } catch (e: Exception) {
            Toast.makeText(mycontext, e.message,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClicked(room: kpica_Room) {
        roomSelected = room
        kpica_RoomListAdapter.selectedRoom = roomSelected
        kpica_RoomListAdapter.notifyDataSetChanged()
        showHideButtons()
    }

    private fun showHideButtons() {
        if (roomSelected.Room == "") {
            btnBooking.visibility = View.INVISIBLE
            btnUnBooking.visibility = View.INVISIBLE
        } else {
            btnBooking.visibility = if (roomSelected.Is_Busy) View.INVISIBLE else View.VISIBLE
            btnUnBooking.visibility = if (roomSelected.Is_Busy) View.VISIBLE else View.INVISIBLE
        }
    }
}