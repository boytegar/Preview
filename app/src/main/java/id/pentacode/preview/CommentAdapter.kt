package id.pentacode.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_comment.view.*

class CommentAdapter(
    val b: List<String>
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var onItemClick : ((Int) -> Unit)? = null
    var index = 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = b[position]

        holder.bind(name, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(name: String,  position: Int) {
            itemView.txt_comment.text = name
        }
    }
    override fun onCreateViewHolder(views: ViewGroup, position: Int): ViewHolder {
        val layoutInflater =
            LayoutInflater.from(views.context).inflate(R.layout.list_comment, views, false)
        return ViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return b.size
    }
}