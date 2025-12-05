package adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.frank_Room

class frank_RoomsAdapter(
    private var rooms: List<frank_Room>,
    private val onRoomClick: (frank_Room) -> Unit
) : RecyclerView.Adapter<frank_RoomsAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.frank_room_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room, onRoomClick)
    }

    override fun getItemCount() = rooms.size

    fun updateData(newRooms: List<frank_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.frank_room_name)
        private val roomCapacity: TextView = itemView.findViewById(R.id.frank_room_capacity)
        private val roomStatus: TextView = itemView.findViewById(R.id.frank_room_status)

        fun bind(room: frank_Room, onRoomClick: (frank_Room) -> Unit) {
            roomName.text = room.room
            roomCapacity.text = "Capacidad: ${room.capacity}"
            itemView.setOnClickListener { onRoomClick(room) }

            if (room.is_busy) {
                roomStatus.text = "Ocupada por: ${room.user}"
                itemView.setBackgroundColor(Color.parseColor("#FFCDD2")) // Red tint
            } else {
                roomStatus.text = "Disponible"
                itemView.setBackgroundColor(Color.parseColor("#C8E6C9")) // Green tint
            }
        }
    }
}