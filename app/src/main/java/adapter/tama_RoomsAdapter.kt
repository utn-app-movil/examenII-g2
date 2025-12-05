package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.tama_Room

class tama_RoomsAdapter(
    private var tama_rooms: List<tama_Room>,
    private val tama_click: (tama_Room) -> Unit
) : RecyclerView.Adapter<tama_RoomsAdapter.TamaVH>() {

    inner class TamaVH(view: View) : RecyclerView.ViewHolder(view) {
        val tama_tvName: TextView = view.findViewById(R.id.tama_tvRoomName)
        val tama_tvStatus: TextView = view.findViewById(R.id.tama_tvRoomStatus)
        val tama_root: LinearLayout = view.findViewById(R.id.tama_rootRoomItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TamaVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tama_room, parent, false)
        return TamaVH(v)
    }

    override fun onBindViewHolder(holder: TamaVH, position: Int) {
        val room = tama_rooms[position]

        holder.tama_tvName.text = room.name
        holder.tama_tvStatus.text =
            if (room.reserved) "Reservada" else "Disponible"

        holder.tama_root.setOnClickListener { tama_click(room) }
    }

    override fun getItemCount() = tama_rooms.size

    fun tama_updateRooms(list: List<tama_Room>) {
        tama_rooms = list
        notifyDataSetChanged()
    }
}