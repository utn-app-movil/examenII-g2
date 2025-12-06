package adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.ahi_Room


class ahi_RoomAdapter(
    private val rooms: List<ahi_Room>,
    private val onClick: (ahi_Room) -> Unit
) : RecyclerView.Adapter<ahi_RoomAdapter.RoomVH>() {

    class RoomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_ahi_room_name)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_ahi_room_status)
        val tvCapacity: TextView = itemView.findViewById(R.id.tv_ahi_room_capacity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ahi_room, parent, false)
        return RoomVH(v)
    }

    override fun onBindViewHolder(h: RoomVH, pos: Int) {
        val r = rooms[pos]
        h.tvName.text = r.room
        h.tvCapacity.text = "Capacidad: ${r.capacity}"
        if (r.is_busy) {
            h.tvStatus.text = "OCUPADA por ${r.user}"
            h.tvStatus.setTextColor(Color.RED)
        } else {
            h.tvStatus.text = "DISPONIBLE"
            h.tvStatus.setTextColor(Color.GREEN)
        }
        h.itemView.setOnClickListener { onClick(r) }
    }

    override fun getItemCount() = rooms.size
}