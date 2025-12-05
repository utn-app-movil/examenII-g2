package adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.alex_Room

class alex_RoomAdapter(
    private var rooms: List<alex_Room>,
    private val currentUser: String,
    private val onActionClick: (alex_Room, Boolean) -> Unit
) : RecyclerView.Adapter<alex_RoomAdapter.alex_RoomViewHolder>() {

    private lateinit var context: Context

    class alex_RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.alex_tv_item_name)
        val tvCapacity: TextView = view.findViewById(R.id.alex_tv_item_capacity)
        val tvStatus: TextView = view.findViewById(R.id.alex_tv_item_status)
        val btnAction: Button = view.findViewById(R.id.alex_btn_item_action)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): alex_RoomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_alex_room, parent, false)
        return alex_RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: alex_RoomViewHolder, position: Int) {
        val room = rooms[position]

        holder.tvName.text = room.room

        val capPrefix = context.getString(R.string.alex_capacity)
        holder.tvCapacity.text = "$capPrefix${room.capacity}"

        if (room.is_busy) {
            holder.tvStatus.text = context.getString(R.string.alex_status_busy)
            holder.tvStatus.setTextColor(Color.RED)

            holder.btnAction.text = context.getString(R.string.alex_action_free)
            holder.btnAction.backgroundTintList = ColorStateList.valueOf(Color.RED)

            holder.btnAction.setOnClickListener { onActionClick(room, false) }
        } else {
            holder.tvStatus.text = context.getString(R.string.alex_status_free)

            val darkGreen = Color.parseColor("#006400")
            holder.tvStatus.setTextColor(darkGreen)

            holder.btnAction.text = context.getString(R.string.alex_action_book)
            holder.btnAction.backgroundTintList = ColorStateList.valueOf(darkGreen)

            holder.btnAction.setOnClickListener { onActionClick(room, true) }
        }
    }

    override fun getItemCount() = rooms.size

    fun updateList(newList: List<alex_Room>) {
        rooms = newList
        notifyDataSetChanged()
    }
}