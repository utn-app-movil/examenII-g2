package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.ken_room

class ken_roomAdapter(private val roomList: MutableList<ken_room>) :
    RecyclerView.Adapter<ken_roomAdapter.KenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KenViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ken_item_room, parent, false)
        return KenViewHolder(view)
    }

    override fun onBindViewHolder(holder: KenViewHolder, position: Int) {
        val item = roomList[position]

        holder.ken_txtRoom.text = item.room
        holder.ken_txtCapacity.text = "Capacidad: ${item.capacity}"
        holder.ken_txtBusy.text = "Ocupada: ${if (item.isBusy) "Sí" else "No"}"
        holder.ken_txtUser.text = "Usuario: ${item.user ?: "N/A"}"
    }

    override fun getItemCount(): Int = roomList.size

    class KenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ken_txtRoom: TextView = itemView.findViewById(R.id.ken_txtRoomItemName)
        val ken_txtCapacity: TextView = itemView.findViewById(R.id.ken_txtRoomItemCapacity)
        val ken_txtBusy: TextView = itemView.findViewById(R.id.ken_txtRoomItemStatus)
        val ken_txtUser: TextView = itemView.findViewById(R.id.ken_txtRoomItemUser)
    }
}
