package cr.ac.utn.appmovil.rooms.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.identities.jor_Room

class jor_RoomAdapter(
    private val rooms: MutableList<jor_Room>,
    private val onClick: (jor_Room) -> Unit
) : RecyclerView.Adapter<jor_RoomAdapter.jor_ViewHolder>() {

    class jor_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jor_tv_name: TextView = itemView.findViewById(R.id.jor_tv_name)
        val jor_tv_capacity: TextView = itemView.findViewById(R.id.jor_tv_capacity)
        val jor_tv_status: TextView = itemView.findViewById(R.id.jor_tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): jor_ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jor_room, parent, false)
        return jor_ViewHolder(view)
    }

    override fun onBindViewHolder(holder: jor_ViewHolder, position: Int) {
        val room = rooms[position]

        holder.jor_tv_name.text = room.room
        holder.jor_tv_capacity.text = "Capacity: ${room.capacity}"

        if (room.is_busy) {
            holder.jor_tv_status.text = "BOOKED by ${room.user}"
            holder.jor_tv_status.setTextColor(android.graphics.Color.RED)
        } else {
            holder.jor_tv_status.text = "AVAILABLE"
            holder.jor_tv_status.setTextColor(android.graphics.Color.GREEN)
        }

        holder.itemView.setOnClickListener { onClick(room) }
    }

    override fun getItemCount() = rooms.size

    fun updateRooms(newRooms: List<jor_Room>) {
        rooms.clear()
        rooms.addAll(newRooms)
        notifyDataSetChanged()
    }
}