package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.edi_Room

class edi_RoomListAdapter(
    private val edi_rooms: MutableList<edi_Room>,
    private val edi_listener: edi_OnRoomClickListener
) : RecyclerView.Adapter<edi_RoomListAdapter.edi_RoomViewHolder>() {

    interface edi_OnRoomClickListener {
        fun edi_onRoomClick(room: edi_Room)
    }

    inner class edi_RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edi_roomNameTextView: TextView = itemView.findViewById(R.id.edi_tv_room_name)
        val edi_capacityTextView: TextView = itemView.findViewById(R.id.edi_tv_capacity)
        val edi_statusTextView: TextView = itemView.findViewById(R.id.edi_tv_status)
        val edi_container: LinearLayout = itemView.findViewById(R.id.edi_room_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): edi_RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edi_room, parent, false)
        return edi_RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: edi_RoomViewHolder, position: Int) {
        val room = edi_rooms[position]
        holder.edi_roomNameTextView.text = room.roomName
        holder.edi_capacityTextView.text =
            holder.itemView.context.getString(R.string.edi_room_capacity, room.capacity)

        if (room.isBusy) {
            holder.edi_statusTextView.text =
                holder.itemView.context.getString(
                    R.string.edi_room_status_busy,
                    room.reservedBy ?: ""
                )
            holder.edi_container.setBackgroundResource(R.color.edi_busy_room_background)
        } else {
            holder.edi_statusTextView.text =
                holder.itemView.context.getString(R.string.edi_room_status_available)
            holder.edi_container.setBackgroundResource(R.color.edi_available_room_background)
        }

        holder.itemView.setOnClickListener {
            edi_listener.edi_onRoomClick(room)
        }
    }

    override fun getItemCount(): Int = edi_rooms.size

    fun edi_updateRooms(newRooms: List<edi_Room>) {
        edi_rooms.clear()
        edi_rooms.addAll(newRooms)
        notifyDataSetChanged()
    }
}