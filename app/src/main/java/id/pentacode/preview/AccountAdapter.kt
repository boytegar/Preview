package id.pentacode.preview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_user.view.*
import android.widget.TextView
import java.nio.file.Files.size
import android.widget.Toast
import id.pentacode.preview.db.entity.User
import java.util.*


class AccountAdapter(val list: List<User>, val layout: Int) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    var onItemClick: ((User) -> Unit)? = null
    var id = -1
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context).inflate(layout, p0, false)
        return ViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list!![position]
        holder.bind(item, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(room: User, pos: Int) {
           // val name =
            if(room.id == id){
                itemView.imageView2.setImageResource(R.drawable.ic_user_selected)
            }else{
                itemView.imageView2.setImageResource(R.drawable.ic_user)
            }

            itemView.txt_name.text = room.name
                itemView.setOnClickListener {
                    onItemClick?.invoke(room)
                }
        }
    }
}