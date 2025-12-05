package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.cagui_Room

class cagui_RoomAdapter(
    private var rooms: List<cagui_Room>,
    private val onRoomClick: (cagui_Room) -> Unit
) : RecyclerView.Adapter<cagui_RoomAdapter.RoomViewHolder>() {

    class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardRoom: CardView = view.findViewById(R.id.cagui_cardRoom)
        val tvRoomName: TextView = view.findViewById(R.id.cagui_tvRoomName)
        val tvRoomStatus: TextView = view.findViewById(R.id.cagui_tvRoomStatus)
        val tvRoomDescription: TextView = view.findViewById(R.id.cagui_tvRoomDescription)
        val tvRoomCapacity: TextView = view.findViewById(R.id.cagui_tvRoomCapacity)
        val tvRoomFloor: TextView = view.findViewById(R.id.cagui_tvRoomFloor)
        val tvRoomBuilding: TextView = view.findViewById(R.id.cagui_tvRoomBuilding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cagui_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        val context = holder.itemView.context

        holder.tvRoomName.text = room.room
        holder.tvRoomDescription.text = if (room.user.isNullOrEmpty()) "No user assigned" else "User: ${room.user}"
        holder.tvRoomCapacity.text = context.getString(R.string.cagui_capacity, room.capacity)
        holder.tvRoomFloor.text = if (room.date.isNullOrEmpty()) "No date" else "Date: ${room.date}"
        holder.tvRoomBuilding.text = ""

        if (room.isBusy) {
            holder.tvRoomStatus.text = context.getString(R.string.cagui_room_booked)
            holder.tvRoomStatus.setTextColor(context.getColor(android.R.color.holo_red_dark))
            holder.tvRoomStatus.setBackgroundColor(context.getColor(android.R.color.holo_red_light))
        } else {
            holder.tvRoomStatus.text = context.getString(R.string.cagui_room_available)
            holder.tvRoomStatus.setTextColor(context.getColor(android.R.color.holo_green_dark))
            holder.tvRoomStatus.setBackgroundColor(context.getColor(android.R.color.holo_green_light))
        }

        holder.cardRoom.setOnClickListener {
            onRoomClick(room)
        }
    }

    override fun getItemCount(): Int = rooms.size

    fun updateRooms(newRooms: List<cagui_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}
