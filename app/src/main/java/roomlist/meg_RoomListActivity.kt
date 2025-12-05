package cr.ac.utn.appmovil.rooms.ui.roomlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.adapter.MegRoomAdapter
import cr.ac.utn.appmovil.rooms.viewmodel.meg_RoomViewModel
import kotlin.collections.emptyList

class meg_RoomListActivity : ComponentActivity() {
    private val vm: meg_RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meg_roomlist_activity)


        val recycler = findViewById<RecyclerView>(R.id.meg_recycler_rooms)
        val adapter = MegRoomAdapter(emptyList())
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter


        vm.rooms.observe(this) { list ->
            adapter.update(list)
        }


        vm.message.observe(this) { msg ->
            if (!msg.isNullOrEmpty()) {

            }
        }

        vm.loadRooms()
    }
}
