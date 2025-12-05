package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.emur_Room


interface emur_RoomActionListener {
    fun onReserveClicked(room: emur_Room)
    fun onUnbookClicked(room: emur_Room)
}

class emur_RoomAdapter(
    private var rooms: List<emur_Room>,
    private val listener: emur_RoomActionListener,
    private val currentUsername: String
) : RecyclerView.Adapter<emur_RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomName: TextView = view.findViewById(R.id.emur_roomNameTextView)
        val capacity: TextView = view.findViewById(R.id.emur_capacityTextView)
        val status: TextView = view.findViewById(R.id.emur_statusTextView)
        val actionButton: Button = view.findViewById(R.id.emur_actionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emur_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.roomName.text = "Sala: ${room.room}"
        holder.capacity.text = "Capacidad: ${room.capacity}"

        val context = holder.itemView.context

        if (room.is_busy) {
            holder.status.text = "Ocupada por: ${room.user}"
            holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

            if (room.user == currentUsername) {
                holder.actionButton.text = "Liberar"
                holder.actionButton.isEnabled = true
                holder.actionButton.setOnClickListener { listener.onUnbookClicked(room) }
            } else {
                holder.actionButton.text = "Reservada"
                holder.actionButton.isEnabled = false
                holder.actionButton.setOnClickListener(null)
            }
        } else {
            holder.status.text = "Disponible"
            holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))

            holder.actionButton.text = "Reservar"
            holder.actionButton.isEnabled = true
            holder.actionButton.setOnClickListener { listener.onReserveClicked(room) }
        }
    }

    override fun getItemCount() = rooms.size

    fun updateRooms(newRooms: List<emur_Room>) {
        this.rooms = newRooms
        notifyDataSetChanged() // Método reconocido
    }
}