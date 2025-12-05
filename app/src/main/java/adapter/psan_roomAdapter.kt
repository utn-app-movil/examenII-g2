package adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.psan_room


class psan_roomAdapter(
    private val rooms: List<psan_room>,
    private val onClick: (psan_room) -> Unit
) : RecyclerView.Adapter<psan_roomAdapter.RoomViewHolder>() {

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lblRoom: TextView = view.findViewById(R.id.lblRoom)
        val lblStatus: TextView = view.findViewById(R.id.lblStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_psan_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.lblRoom.text = room.room
        holder.lblStatus.text = if (room.is_busy) "Ocupada" else "Disponible"
        holder.itemView.setBackgroundColor(
            if (room.is_busy) 0xFFFF9999.toInt() else 0xFF99FF99.toInt()
        )

        holder.itemView.setOnClickListener { onClick(room) }
    }
}
