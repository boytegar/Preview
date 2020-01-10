package id.pentacode.preview

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.pentacode.preview.Helper.ItemTouchHelperAdapter
import id.pentacode.preview.Helper.SharedData
import id.pentacode.preview.db.entity.DataAllImage
import id.pentacode.preview.db.entity.DateImage
import id.pentacode.preview.viewmodel.DataImageViewModel
import kotlinx.android.synthetic.main.list_foto.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class FotoAdapterNew(
    val context: Context,
    val layout: Int,
    val list_data: ArrayList<Int>,
    val list_date: ArrayList<DateImage>,
    val viewModel: DataImageViewModel,
   val userId: Int
) : RecyclerView.Adapter<FotoAdapterNew.PersonViewHolder>(), ItemTouchHelperAdapter {
    var dateVisible = 1
    var onItemClick: ((Array<Any>) -> Unit)? = null
    var list_ = ArrayList<DataAllImage>()
    var list_new = ArrayList<Int>()
    var list_old = ArrayList<Int>()
    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
//            if (fromPosition < toPosition) {
//                val from = list_data[fromPosition]
//                val to = list_data[toPosition]
////                list_data.remove(from)
////                list_data.add(to)
//                for (i in fromPosition until toPosition) {
//                    //   swap(list_data, i, i + 1)
//
//                    val order1 = list_data[i]
//                    //    list_.add(order1)
////                    list_new.add(order2)
////                    list_old.add(order1)
//                    Log.e("POSITION", order1.position.toString())
//                }
//                //   list_.add(from)
//                // viewModel.updatePosition(list_new, list_old)
//            } else {
//                var from = list_data[fromPosition]
//                val to = list_data[toPosition]
//                //  SharedData.setKeyString(context,"list", list_data.toString())
//                for (i in fromPosition downTo toPosition + 1) {
////                    swap(list_data, i, i - 1)
//
////                    val movedItem = list_data.get(fromPosition)
////                    list_data.remove(from)
////                    list_data.add(toPosition, movedItem)
//
//                    val order1 = list_data[i]
//                    //   list_.add(order1)
////                    list_data[i] = order1
////                    list_data[i - 1] = order2
//                    Log.e("POSITION", order1.position.toString())
//                }
//                //  list_.add(from)
//                // viewModel.updatePosition(list_new, list_old)
//            }
        var from = list_data[fromPosition]
        val to = list_data[toPosition]
        val movedItem = list_data.get(fromPosition)
        list_data.remove(from)
        list_data.add(toPosition, movedItem)
        notifyItemMoved(fromPosition, toPosition)

    }

    override fun onRowSelected(myViewHolder: PersonViewHolder) {
        myViewHolder.image_view.setPadding(25, 25, 25, 25)
    }

    override fun onRowClear(myViewHolder: PersonViewHolder) {
        myViewHolder.image_view.setPadding(0, 0, 0, 0)
        SharedData.setKeyString(context,"list$userId", list_data.toString())
        SharedData.setKeyInt(context,"number",0)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list_data.size
    }


    override fun onBindViewHolder(
        holderPerson: PersonViewHolder,
        position: Int
    ) {
        var person = list_data[position]

        holderPerson.bind(person)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(LayoutInflater.from(context).inflate(layout, parent, false))
    }

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image_view = view.imageView
        val txt_date = view.txt_date
        val txt_day = view.txt_day
        val myView = view
        val holder = view
        fun bind(
            prof: Int
        ) {
            var profile: DataAllImage
            doAsync {
                val a = viewModel.getDatabyId(prof)
                uiThread {
                    profile = a
                    val decodedString = Base64.decode(profile.image, Base64.NO_WRAP)
                    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    txt_day.text = profile.day
                    txt_date.text = profile.date
                    Glide.with(itemView.context)
                        .load(decodedByte)
                        .into(image_view)

                    when (dateVisible) {
                        1 -> {
                            txt_date.visibility = View.VISIBLE
                            txt_day.visibility = View.VISIBLE
                        }
                        else -> {
                            txt_date.visibility = View.GONE
                            txt_day.visibility = View.GONE
                        }
                    }
                    val array = arrayOf(profile, profile.id!!)
                    holder.setOnClickListener {
                        onItemClick!!.invoke(array)
                    }
                }
            }


        }
    }

}