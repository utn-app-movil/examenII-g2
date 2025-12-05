package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SebasRoomAdapter(
    private var rooms: List<SebasRoom>,
    private val onRoomClick: (SebasRoom) -> Unit
) : RecyclerView.Adapter<SebasRoomAdapter.SebasRoomViewHolder>() {

    inner class SebasRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val roomName: TextView = itemView.findViewById(R.id.sebas_tv_room_name)
        val roomCapacity: TextView = itemView.findViewById(R.id.sebas_tv_room_capacity)
        val reservedBy: TextView = itemView.findViewById(R.id.sebas_tv_reserved_by)
        val actionButton: Button = itemView.findViewById(R.id.sebas_btn_action)

        fun bind(room: SebasRoom) {
            roomName.text = room.room
            roomCapacity.text = "Capacity: ${room.capacity}"
            
            if (room.isBusy) {
                reservedBy.visibility = TextView.VISIBLE
                reservedBy.text = "Reserved by: ${room.user}"
                actionButton.text = "Release"
            } else {
                reservedBy.visibility = TextView.GONE
                actionButton.text = "Book"
            }
            
            actionButton.setOnClickListener {
                onRoomClick(room)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SebasRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sebas_item_room, parent, false)
        return SebasRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: SebasRoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    fun updateRooms(newRooms: List<SebasRoom>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}
