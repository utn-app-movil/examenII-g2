package cr.ac.utn.appmovil.rooms.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.kris_Room

class kris_RoomAdapter(
    private var rooms: List<kris_Room>,
    private val onActionClick: (kris_Room) -> Unit
) : RecyclerView.Adapter<kris_RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRoomName: TextView = view.findViewById(R.id.kris_txtRoomName)
        val txtRoomCapacity: TextView = view.findViewById(R.id.kris_txtRoomCapacity)
        val btnAction: Button = view.findViewById(R.id.kris_btnAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.txtRoomName.text = room.room
        holder.txtRoomCapacity.text = "Capacity: ${room.capacity}"

        if (room.is_busy) {
            holder.btnAction.text = "Release"
            holder.btnAction.setBackgroundColor(Color.RED)
        } else {
            holder.btnAction.text = "Reserve"
            holder.btnAction.setBackgroundColor(Color.GREEN)
        }

        holder.btnAction.setOnClickListener { onActionClick(room) }
    }

    override fun getItemCount(): Int = rooms.size

    fun updateData(newRooms: List<kris_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}
