package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.mich_Room

class mich_RoomAdapter(
    private val rooms: List<mich_Room>,
    private val onActionClick: (mich_Room) -> Unit
) : RecyclerView.Adapter<mich_RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRoomName: TextView = view.findViewById(R.id.tvRoomName)
        val tvCapacity: TextView = view.findViewById(R.id.tvCapacity)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAction: Button = view.findViewById(R.id.btnAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mich_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.tvRoomName.text = room.room
        holder.tvCapacity.text = "Capacidad: ${room.capacity}"

        if (room.is_busy) {
            holder.tvStatus.text = "Estado: Ocupada por ${room.user}"
            holder.btnAction.text = "Liberar"
        } else {
            holder.tvStatus.text = "Estado: Disponible"
            holder.btnAction.text = "Reservar"
        }

        holder.btnAction.setOnClickListener {
            onActionClick(room)
        }
    }

    override fun getItemCount(): Int = rooms.size
}

