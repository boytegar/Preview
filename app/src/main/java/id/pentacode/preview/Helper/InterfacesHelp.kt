package id.pentacode.preview.Helper


import id.pentacode.preview.FotoAdapterNew


interface OnStartDragListener {

    fun onStartDrag(viewHolder : FotoAdapterNew.PersonViewHolder)

}


interface ItemTouchHelperAdapter {
    fun onBindViewHolder(holder: FotoAdapterNew.PersonViewHolder, position: Int)
    fun onRowMoved(fromPosition: Int, toPosition: Int)
    fun onRowSelected(myViewHolder: FotoAdapterNew.PersonViewHolder)
    fun onRowClear(myViewHolder: FotoAdapterNew.PersonViewHolder)
}