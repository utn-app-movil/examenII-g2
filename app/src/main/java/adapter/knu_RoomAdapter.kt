package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.model.knu_Room

class knu_RoomAdapter(
    private var rooms: MutableList<knu_Room>,
    private val onItemClick: (knu_Room) -> Unit
) : RecyclerView.Adapter<knu_RoomAdapter.knu_RoomViewHolder>() {

    fun knu_setData(newRooms: List<knu_Room>) {
        rooms.clear()
        rooms.addAll(newRooms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): knu_RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_knu_room, parent, false)
        return knu_RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: knu_RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
        holder.itemView.setOnClickListener { onItemClick(room) }
    }

    override fun getItemCount(): Int = rooms.size

    class knu_RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.knu_txtRoomName)
        private val txtCapacity: TextView = itemView.findViewById(R.id.knu_txtRoomCapacity)
        private val txtStatus: TextView = itemView.findViewById(R.id.knu_txtRoomStatus)

        fun bind(room: knu_Room) {
            txtName.text = room.room
            txtCapacity.text = "Capacity: ${room.capacity}"
            txtStatus.text = if (room.is_busy) {
                "Status: Busy (${room.user ?: "N/A"})"
            } else {
                "Status: Available"
            }
        }
    }
}
