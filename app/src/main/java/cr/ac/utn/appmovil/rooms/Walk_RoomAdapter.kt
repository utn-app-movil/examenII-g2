package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Walk_RoomAdapter(
    private val rooms: List<Walk_Room>,
    private val onReserve: (Walk_Room) -> Unit,
    private val onUnreserve: (Walk_Room) -> Unit
) : RecyclerView.Adapter<Walk_RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomName: TextView = view.findViewById(R.id.walk_item_room_name)
        val roomStatus: TextView = view.findViewById(R.id.walk_item_room_status)
        val actionButton: Button = view.findViewById(R.id.walk_item_room_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.walk_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.roomName.text = room.room

        val currentEmail = Walk_Session.currentUser?.email ?: ""

        if (room.is_busy) {
            holder.roomStatus.text = "Reserved by: ${room.user}"

            // ✔ Comparación correcta usando EMAIL
            if (room.user == currentEmail) {
                holder.actionButton.text = "Unreserve"
                holder.actionButton.isEnabled = true
                holder.actionButton.setOnClickListener { onUnreserve(room) }
            } else {
                holder.actionButton.text = "Unavailable"
                holder.actionButton.isEnabled = false
            }

        } else {
            holder.roomStatus.text = "Available"
            holder.actionButton.text = "Reserve"
            holder.actionButton.isEnabled = true
            holder.actionButton.setOnClickListener { onReserve(room) }
        }
    }

    override fun getItemCount(): Int = rooms.size
}
