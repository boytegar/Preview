package id.pentacode.preview

import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import id.pentacode.preview.Helper.SharedData
import id.pentacode.preview.viewmodel.DataImageViewModel
import kotlinx.android.synthetic.main.activity_photo.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.io.File


class PhotoActivity : AppCompatActivity() {

    lateinit var dataImageViewModel: DataImageViewModel
    val list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
       // setSupportActionBar(toolbar)
       // title = "Photo"
        dataImageViewModel = ViewModelProviders.of(this).get(DataImageViewModel::class.java)
        val i = intent
        val day = i.getStringExtra("day")
        val date = i.getStringExtra("date")
        val user_id = i.getIntExtra("pos", 0)
        val pos = i.getIntExtra("ids", 0)
        val path = i.getStringExtra("path")

        txt_date.text = date
        txt_day.text = day
        val file = File(path)
        val imageUri = Uri.fromFile(file)
        Glide.with(this)
            .load(imageUri)
            .into(img_photo)

        Log.e("PATH", path)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height_screen = displayMetrics.heightPixels
        val width_screen = displayMetrics.widthPixels
        initializeBottomSheet(height_screen, width_screen)
        initComment()

        btn_delete.setOnClickListener {
            toast("clicked")
            doAsync {
                val list_d = ArrayList<Int>()
                val datas = SharedData.getKeyString(this@PhotoActivity, "list$user_id")!!
                val favs2 = datas.replace("\\[|\\]| ".toRegex(), "").split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                for (i in favs2.indices) {
                    list_d.add(Integer.parseInt(favs2[i]))
                }

                dataImageViewModel.deleteById(pos)
                runOnUiThread {
                    Log.e("LIST D", datas.toString())
                    list_d.remove(pos)
                    SharedData.setKeyString(this@PhotoActivity, "list$user_id", list_d.toString())
                    SharedData.setKeyInt(this@PhotoActivity, "delete_status", 1)
                    toast("Delete Success")
                    finish()
                }
            }
        }
    }


    fun initComment(){

        list.add("Kurangin Cahayanya")
        list.add("Fotonya kurang Jernih")
        val adapterNew = CommentAdapter(list)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val linearLayoutManager = layoutManager
        list_comment.layoutManager = linearLayoutManager
        list_comment.hasFixedSize()
        list_comment.adapter = adapterNew

        btn_send.setOnClickListener {
            val com = edt_comment.text.toString()
            list.add(com)
            adapterNew.notifyDataSetChanged()
        }
    }

    private fun initializeBottomSheet(heightScreen: Int, widthScreen: Int) { // init the bottom sheet behavior
        val bottomSheet: View = findViewById(R.id.bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        // change the state of the bottom sheet
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // set callback for changes
        bottomSheetBehavior.peekHeight = (heightScreen/2.05).toInt()
        val params: ViewGroup.LayoutParams = list_comment.layoutParams
        params.height = (heightScreen/3.65).toInt()
        list_comment.layoutParams = params

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    params.height = (heightScreen/3.65).toInt()
                    list_comment.layoutParams = params
                }else{
                    params.height = (heightScreen/1.28).toInt()
                    list_comment.layoutParams = params
                }

            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    layout_photo.alpha = 1-(slideOffset)
            }
        })
    }
}
