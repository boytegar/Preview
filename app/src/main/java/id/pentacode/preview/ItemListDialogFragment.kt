package id.pentacode.preview

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.view.*
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat.animate





class ItemListDialogFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


//    companion object {
//
//        // TODO: Customize parameters
//        fun newInstance(itemCount: Int): ItemListDialogFragment =
//            ItemListDialogFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }

//    }
}
