package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import interfaces.kpica_OnItemClickListener
import kpica_Entity.kpica_Room

class kpica_CustomViewHolder (view: View): RecyclerView.ViewHolder(view){
    var txtRoomName: TextView = view.findViewById(R.id.kpica_txtRoomNameItem_recycler)
    var txtCapacity: TextView = view.findViewById(R.id.kpica_txtCapacityItem_recycler)
    var txtUser: TextView = view.findViewById(R.id.kpica_txtUserItem_recycler)
    var txtDate: TextView = view.findViewById(R.id.kpica_txtDateItem_recycler)

    fun bind (item: kpica_Room, clickListener: kpica_OnItemClickListener, isSelected: Boolean = false) {
        val context = itemView.context
        txtRoomName.setText("${context.getString(R.string.kpica_RoomName)} ${item.Room.toString()}")
        txtCapacity.setText("${context.getString(R.string.kpica_RoomCapacity)} ${item.Capacity.toString()}")
        txtUser.setText("${context.getString(R.string.kpica_RoomUser)} ${item.User.toString()}")
        txtDate.setText(if (item.Date == null) "Sin reserva" else "${context.getString(R.string.kpica_RoomDate)} ${item.Date.toString()}")

        if (isSelected) {
            itemView.setBackgroundColor(context.getColor(R.color.teal_200))
        } else {
            itemView.setBackgroundColor(context.getColor(R.color.purple_500))
        }

        itemView.setOnClickListener{
            clickListener.onItemClicked(item)
        }
    }
}

class kpica_RoomListAdapter (private var itemList: List<kpica_Room>,
    val itemClickListener: kpica_OnItemClickListener): RecyclerView.Adapter<kpica_CustomViewHolder>() {

    var selectedRoom: kpica_Room? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): kpica_CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.kpica_room_item_activity, parent, false)
        return kpica_CustomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: kpica_CustomViewHolder, position: Int) {
        var item = itemList[position]
        holder.bind(item, itemClickListener, item == selectedRoom)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}