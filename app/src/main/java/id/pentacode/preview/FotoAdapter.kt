package id.pentacode.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.pentacode.preview.Helper.DataImageCallback
import id.pentacode.preview.db.entity.DataAllImage
import kotlinx.android.synthetic.main.list_foto.view.*

import android.graphics.BitmapFactory
import android.util.Base64

import androidx.paging.PagedList
import id.pentacode.preview.Helper.ItemTouchHelperAdapter
import id.pentacode.preview.Helper.OnStartDragListener
import id.pentacode.preview.db.entity.DateImage
import id.pentacode.preview.viewmodel.DataImageViewModel
import java.util.Collections.swap
import kotlin.collections.ArrayList


class FotoAdapter(
    val context: Context,
    val layout: Int,
    val it: List<DateImage>,
    val dragListener: OnStartDragListener,
    val viewModel: DataImageViewModel
) : PagedListAdapter<DataAllImage, FotoAdapter.PersonViewHolder>(DataImageCallback()), ItemTouchHelperAdapter {

   var list = ArrayList<DataAllImage>()

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < list.size && toPosition < list.size) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    swap(list, i, i + 1)

                    val order1 = list[i]
                    val order2 = list[i + 1]
                    list[i] = order1
                    list[i + 1] = order2
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    swap(list, i, i - 1)

                    val order1 = list[i]
                    val order2 = list[i - 1]
                    list[i] = order1
                    list[i - 1] = order2
                }
            }
            notifyItemMoved(fromPosition, toPosition)

        }
        return true
    }

//    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//      //  viewModel.updateAll(list as List<DataAllImage>)
//    }

    override fun onItemDismiss(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun submitList(pagedList: PagedList<DataAllImage>?) {

        for (i in 0 until pagedList!!.size){
            val data = DataAllImage()
            data.id = pagedList[i]!!.id
            data.image = pagedList[i]!!.image
            data.group_id = pagedList[i]!!.group_id
            data.caption = pagedList[i]!!.caption
            data.path = pagedList[i]!!.path
            list.add(data)
        }

        super.submitList(pagedList)
    }

    var onItemClick: ((DataAllImage) -> Unit)? = null
    //  var view
    override fun onBindViewHolder(holderPerson: PersonViewHolder, position: Int) {
        var person = getItem(position)
        var date = it[position]
        if (person == null) {
            holderPerson.clear()
        } else {
            holderPerson.bind(person, date)
        }

//        holderPerson.image_view.setOnLongClickListener {
//
//                dragListener.onStartDrag(holderPerson!!)
//
//            return@setOnLongClickListener false
//        }

//        holderPerson.image_view.setOnTouchListener { v, event ->
//
//            v.onTouchEvent(event)
//            // We're only interested in when the button is released.
//
//            return@setonLong false
//        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(LayoutInflater.from(context).inflate(layout, parent, false))
    }

    fun update(){
        list.clear()
        notifyDataSetChanged()
    }

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image_view = view.imageView
        val txt_date = view.txt_date
        val txt_day = view.txt_day
        val myView = view
        val holder = view
        fun bind(profile: DataAllImage, date: DateImage) {

            val decodedString = Base64.decode(profile.image, Base64.NO_WRAP)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            txt_date.text = date.time
            txt_day.text = date.day

//            image_view.setOnLongClickListener {
//                image_view.setPadding(25, 25, 25, 25)
//                return@setOnLongClickListener true
//            }



            Glide.with(context)
                    .load(decodedByte)
                    .into(image_view)
        }

        fun clear() {
            //   tvName.text = null


        }

    }


}