package cr.ac.utn.appmovil.rooms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.model.MegRoom
import cr.ac.utn.appmovil.rooms.R

class MegRoomAdapter(
    private var rooms: List<MegRoom>,
    private val onBook: (String) -> Unit,
    private val onUnbook: (String) -> Unit

) : RecyclerView.Adapter<MegRoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.meg_room_name)
        val txtCapacity: TextView = view.findViewById(R.id.meg_room_capacity)
        val txtStatus: TextView = view.findViewById(R.id.meg_room_status)
        val btnBook: Button = view.findViewById(R.id.btn_book)
        val btnUnbook: Button = view.findViewById(R.id.btn_unbook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meg_room_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.txtName.text = room.room
        holder.txtCapacity.text = "Capacidad: ${room.capacity}"
        holder.txtStatus.text = if (room.is_busy) "Ocupada" else "Disponible"

        holder.btnBook.setOnClickListener { onBook(room.room) }
        holder.btnUnbook.setOnClickListener { onUnbook(room.room) }
    }

    override fun getItemCount() = rooms.size

    fun update(newRooms: List<MegRoom>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}
