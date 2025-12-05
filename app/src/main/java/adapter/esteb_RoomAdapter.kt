package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.esteb_Room

class esteb_RoomAdapter(
    private var rooms: List<esteb_Room>,
) : RecyclerView.Adapter<esteb_RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoom: TextView = itemView.findViewById(R.id.tvRoom)
        val tvCapacity: TextView = itemView.findViewById(R.id.tvCapacity)
        val tvBusy: TextView = itemView.findViewById(R.id.tvBusy)
        val tvUser: TextView = itemView.findViewById(R.id.tvUser)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val card: CardView = itemView.findViewById(R.id.cardRoom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.esteb_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.tvRoom.text = room.room
        holder.tvCapacity.text = "Capacity: ${room.capacity}"
        holder.tvBusy.text = if (room.is_busy) "Busy" else "Available"
        holder.tvUser.text = "User: ${room.user ?: "-"}"
        holder.tvDate.text = "Date: ${room.date ?: "-"}"

        // Colores dinámicos
        if (room.is_busy) {
            holder.card.setCardBackgroundColor(0xFFFFD1D1.toInt()) // rojo claro
        } else {
            holder.card.setCardBackgroundColor(0xFFD1FFD9.toInt()) // verde claro
        }
    }

    fun updateData(newList: List<esteb_Room>) {
        rooms = newList
        notifyDataSetChanged()
    }
}
