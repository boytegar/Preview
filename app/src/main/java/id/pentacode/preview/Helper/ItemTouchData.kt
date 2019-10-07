package id.pentacode.preview.Helper

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import id.pentacode.preview.FotoAdapter
import id.pentacode.preview.FotoAdapterNew
import id.pentacode.preview.db.entity.DataAllImage
import java.util.*
import kotlin.collections.ArrayList

class ItemTouchData(val adapter: FotoAdapterNew, val list: ArrayList<DataAllImage>): ItemTouchHelper.Callback() {


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
      //  adapter.onItemDismiss(direction)
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //return super.canDropOver(recyclerView, current, target)
        return target.itemViewType == current.itemViewType
    }
//    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        super.clearView(recyclerView, viewHolder)
//        adapter.clearView(recyclerView, viewHolder)
//    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }

    //defines the enabled move directions in each state (idle, swiping, dragging).
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // The position i want to lock/halt
//        if (list[viewHolder.adapterPosition].toString().isEmpty()) {
//            return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.DOWN or ItemTouchHelper.UP)
//        }
//        // The position i want to lock/halt
//        if (viewHolder.adapterPosition == list.size) {
//            return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.DOWN or ItemTouchHelper.UP)
//        }
//        // else enabling ACTION_STATE_DRAG
//        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
//                ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END)

        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)

    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val width = viewHolder.itemView.width.toFloat()
            val alpha = 1.0f - Math.abs(dX) / width
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive)
        }
    }
}