package id.pentacode.preview.Helper

import androidx.recyclerview.widget.RecyclerView
import id.pentacode.preview.FotoAdapter
import id.pentacode.preview.FotoAdapterNew


interface OnStartDragListener {

    fun onStartDrag(viewHolder : FotoAdapterNew.PersonViewHolder)

}


interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)

    fun onItemSelected(viewHolder : FotoAdapterNew.PersonViewHolder)

    fun onItemClear(viewHolder : FotoAdapterNew.PersonViewHolder)
   // fun clearView( recyclerView: RecyclerView,  viewHolder : RecyclerView.ViewHolder)
}