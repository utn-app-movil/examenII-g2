package cr.ac.utn.appmovil.rooms.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.model.SergRoom

class SergRoomAdapter(
    private var rooms: List<SergRoom>,
    private val onBookClick: (SergRoom) -> Unit,
    private val onUnbookClick: (SergRoom) -> Unit
) : RecyclerView.Adapter<SergRoomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomName: TextView = view.findViewById(R.id.serg_tv_room_name)
        val status: TextView = view.findViewById(R.id.serg_tv_status)
        val capacity: TextView = view.findViewById(R.id.serg_tv_capacity)
        val user: TextView = view.findViewById(R.id.serg_tv_user)
        val actionButton: Button = view.findViewById(R.id.serg_btn_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_serg_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = rooms[position]

        holder.roomName.text = room.room
        holder.capacity.text = "Capacity: ${room.capacity}"

        if (room.is_busy) {
            holder.status.text = "Occupied"
            holder.status.setTextColor(Color.RED)
            holder.user.visibility = View.VISIBLE
            holder.user.text = "User: ${room.user}"
            holder.actionButton.text = "Release"
            holder.actionButton.setOnClickListener { onUnbookClick(room) }
        } else {
            holder.status.text = "Available"
            holder.status.setTextColor(Color.GREEN)
            holder.user.visibility = View.GONE
            holder.actionButton.text = "Book"
            holder.actionButton.setOnClickListener { onBookClick(room) }
        }
    }

    override fun getItemCount() = rooms.size

    fun updateRooms(newRooms: List<SergRoom>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}