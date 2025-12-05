package cr.ac.utn.appmovil.rooms.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.model.carl_Room

class carl_RoomAdapter(
    private var rooms: List<carl_Room>,
    private val onRoomClick: (carl_Room) -> Unit
) : RecyclerView.Adapter<carl_RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.carl_textViewRoomName)
        val textViewCapacity: TextView = view.findViewById(R.id.carl_textViewCapacity)
        val textViewStatus: TextView = view.findViewById(R.id.carl_textViewStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.carl_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.textViewName.text = room.room
        holder.textViewCapacity.text = "Capacity: ${room.capacity}"

        if (room.isBusy) {
            holder.textViewStatus.text = "BOOKED by ${room.user ?: "Unknown"}"
            holder.textViewStatus.setTextColor(Color.RED)
            holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2"))
        } else {
            holder.textViewStatus.text = "AVAILABLE"
            holder.textViewStatus.setTextColor(Color.parseColor("#4CAF50"))
            holder.itemView.setBackgroundColor(Color.parseColor("#C8E6C9"))
        }

        holder.itemView.setOnClickListener {
            onRoomClick(room)
        }
    }

    override fun getItemCount() = rooms.size

    fun updateRooms(newRooms: List<carl_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}