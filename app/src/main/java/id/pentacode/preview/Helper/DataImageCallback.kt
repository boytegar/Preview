package id.pentacode.preview.Helper

import androidx.recyclerview.widget.DiffUtil
import id.pentacode.preview.db.entity.DataAllImage

class DataImageCallback : DiffUtil.ItemCallback<DataAllImage>() {



    override fun areItemsTheSame(oldItem: DataAllImage, newItem: DataAllImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataAllImage, newItem: DataAllImage): Boolean {
        return oldItem.equals(newItem)
    }
}