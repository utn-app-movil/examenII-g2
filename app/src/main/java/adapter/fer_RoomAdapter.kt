package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.fer_Room

class fer_RoomAdapter(
    private var fer_roomList: List<fer_Room>,
    private val fer_onRoomClick: (fer_Room) -> Unit
) : RecyclerView.Adapter<fer_RoomAdapter.fer_RoomViewHolder>() {

    class fer_RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fer_cardView: CardView = itemView.findViewById(R.id.fer_card_room)
        val fer_tvRoomName: TextView = itemView.findViewById(R.id.fer_tv_room_name)
        val fer_tvCapacity: TextView = itemView.findViewById(R.id.fer_tv_capacity)
        val fer_tvStatus: TextView = itemView.findViewById(R.id.fer_tv_status)
        val fer_tvUser: TextView = itemView.findViewById(R.id.fer_tv_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): fer_RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fer_item_room, parent, false)
        return fer_RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: fer_RoomViewHolder, position: Int) {
        val room = fer_roomList[position]

        holder.fer_tvRoomName.text = room.room
        holder.fer_tvCapacity.text = "Capacity: ${room.capacity}"

        if (room.is_busy) {
            holder.fer_tvStatus.text = "OCCUPIED"
            holder.fer_tvStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark)
            )
            holder.fer_cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.fer_room_occupied)
            )
            holder.fer_tvUser.visibility = View.VISIBLE
            holder.fer_tvUser.text = "User: ${room.user}"
        } else {
            holder.fer_tvStatus.text = "AVAILABLE"
            holder.fer_tvStatus.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_green_dark)
            )
            holder.fer_cardView.setCardBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.fer_room_available)
            )
            holder.fer_tvUser.visibility = View.GONE
        }

        holder.fer_cardView.setOnClickListener {
            fer_onRoomClick(room)
        }
    }

    override fun getItemCount(): Int = fer_roomList.size

    fun fer_updateRooms(newRooms: List<fer_Room>) {
        fer_roomList = newRooms
        notifyDataSetChanged()
    }
}
