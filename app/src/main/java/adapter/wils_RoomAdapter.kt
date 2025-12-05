package adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.wils_Room

class wils_RoomAdapter(
    private var rooms: List<wils_Room>,
    private val onRoomClick: (wils_Room) -> Unit
) : RecyclerView.Adapter<wils_RoomAdapter.wils_RoomViewHolder>() {
    
    class wils_RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomName: TextView = itemView.findViewById(R.id.wils_tvRoomName)
        val tvRoomInfo: TextView = itemView.findViewById(R.id.wils_tvRoomInfo)
        val btnRoomAction: Button = itemView.findViewById(R.id.wils_btnRoomAction)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): wils_RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wils_room, parent, false)
        return wils_RoomViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: wils_RoomViewHolder, position: Int) {
        val room = rooms[position]
        
        holder.tvRoomName.text = room.room
        holder.tvRoomInfo.text = "Capacidad: ${room.capacity} personas"
        
        if (room.is_busy) {
            holder.btnRoomAction.text = "LIBERAR"
            holder.btnRoomAction.setBackgroundColor(Color.parseColor("#F44336"))
            holder.btnRoomAction.setTextColor(Color.WHITE)
            if (room.user.isNotEmpty()) {
                holder.tvRoomInfo.text = "${holder.tvRoomInfo.text} • Reservada por: ${room.user}"
            }
        } else {
            holder.btnRoomAction.text = "RESERVAR"
            holder.btnRoomAction.setBackgroundColor(Color.parseColor("#4CAF50"))
            holder.btnRoomAction.setTextColor(Color.WHITE)
        }
        
        holder.btnRoomAction.setOnClickListener {
            onRoomClick(room)
        }
    }
    
    override fun getItemCount(): Int = rooms.size
    
    fun updateRooms(newRooms: List<wils_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }
}