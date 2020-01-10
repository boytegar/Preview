package id.pentacode.preview

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.pentacode.preview.Helper.ItemTouchData
import id.pentacode.preview.Helper.SharedData
import id.pentacode.preview.db.entity.DataAllImage
import id.pentacode.preview.db.entity.DateImage
import id.pentacode.preview.db.entity.User
import id.pentacode.preview.viewmodel.DataImageViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.dialog_insert_image.view.*
import kotlinx.android.synthetic.main.dialog_insert_image.view.txt_desc
import kotlinx.android.synthetic.main.dialog_show_image.view.*
import kotlinx.android.synthetic.main.dialog_show_image.view.img_photo
import kotlinx.android.synthetic.main.fragment_item_list_dialog.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    private val INPUT_FILE_REQUEST_CODE = 1
    private var mCameraPhotoPath: String? = null
    private val REQUEST_CODE_PERMISSIONS = 10
    lateinit var list: ArrayList<Int>
    lateinit var list_date: ArrayList<DateImage>
    lateinit var adapterNew: FotoAdapterNew
    // This is an array of all the permission specified in the manifest
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var list_user: List<User>
    lateinit var viewModel: DataImageViewModel
    lateinit var dates: String
    lateinit var day: String
    var user_id: Int = 0
    var date_visible = 1
    var name_user = ""
    lateinit var list_d: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)

        viewModel = ViewModelProviders.of(this).get(DataImageViewModel::class.java)

        setSupportActionBar(toolbar)
        if (allPermissionsGranted()) {
            //   viewFinder.post { startCamera(lensFacing) }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MMM/yy")
        dates = df.format(c)
        day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis())

        fab.setOnClickListener {
            //  openFile()
            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.type = "image/*"

            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
        }

        btn_date.setOnClickListener {
            when (date_visible) {
                1 -> {
                    date_visible = 0
                    adapterNew.dateVisible = 0
                    changeVisibleDate()
                }
                else -> {
                    date_visible = 1
                    adapterNew.dateVisible = 1
                    changeVisibleDate()
                }
            }
        }

        viewModel.getAllUsers().observe(this, androidx.lifecycle.Observer {
            list_user = it
        })


        user_id = SharedData.getKeyInt(this, "user_id")!!
        if (user_id == 0 || user_id == null) {
            fab.hide()
        } else {
            fab.show()
            getData(user_id!!)
        }

        name_user = SharedData.getKeyString(this, "name")!!
        if (name_user == "" || name_user == null) {
            txt_user.text = "No User, Add Here"
        } else {
            txt_user.text = name_user
        }
        drawer_layout.setScrimColor(Color.TRANSPARENT)
        var actionBarDrawerToggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close) {

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView.getWidth() * slideOffset
                    main.setTranslationX(-slideX)
                }
            }

        drawer_layout.addDrawerListener(actionBarDrawerToggle)

        btn_add.setOnClickListener {
            toast("Cant Action")
        }
        btn_user.setOnClickListener {
            showBottomSheet()
        }
    }


    fun changeVisibleDate() {
        when (date_visible) {
            1 -> {
                btn_date.text = "DATE : VISIBLE"
                btn_date.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_date_visible,
                    0,
                    0,
                    0
                )
                btn_date.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_visible))
                btn_date.setTextColor(Color.parseColor("#ffffff"))
                btn_date.setPadding(40, 0, 40, 0)
            }
            else -> {
                btn_date.text = "DATE : NOT VISIBLE"
                btn_date.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_date_invisible,
                    0,
                    0,
                    0
                )
                btn_date.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_btn_invisible))
                btn_date.setTextColor(Color.parseColor("#3D6CF7"))
                btn_date.setPadding(40, 0, 40, 0)
            }
        }
        adapterNew.notifyDataSetChanged()
    }

    fun getData(user_id: Int) {

        doAsync {
            var list_pref = ArrayList<Int>()
            list = viewModel.getAllImages(user_id) as ArrayList<Int>
            list_date = viewModel.getAllDate(user_id) as ArrayList<DateImage>
            for (i in 0 until  list.size){
                Log.e("ID IMAGE", list[i].toString())
            }
            runOnUiThread {
                val datas = SharedData.getKeyString(this@MainActivity, "list$user_id")
                if (datas == "" || datas == null) {
                    list_pref = list
                } else {
                    list_d = ArrayList()
                    val favs2 = datas.replace("\\[|\\]| ".toRegex(), "").split(",".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    for (i in favs2.indices) {
                        list_d.add(Integer.parseInt(favs2[i]))
                    }
                    Log.e("LIST PREF", "${list_d.size} == ${list.size}")
                    if (list_d.size == list.size) {
                        list_pref = list_d
                        viewModel.list_id.value = list_pref
                        SharedData.setKeyString(this@MainActivity, "list$user_id", list_pref.toString())
                    } else {
                        var numb = SharedData.getKeyInt(this@MainActivity, "number")!!

                        for (i in numb downTo 0){
                            list_pref.add(list[i])
                        }
                        list_pref.addAll(list_d)
                        viewModel.list_id.value = list_pref
                        SharedData.setKeyString(this@MainActivity, "list$user_id", list_pref.toString())
                    }
                }

                Log.e("LIST DATA", list_pref.toString())

                adapterNew = FotoAdapterNew(
                    this@MainActivity,
                    R.layout.list_foto,
                    list_pref,
                    list_date,
                    viewModel,
                    user_id
                )

                val callback = ItemTouchData(adapterNew)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(list_image)
                val linearLayoutManager = GridLayoutManager(this@MainActivity, 3)
                list_image.layoutManager = linearLayoutManager
                list_image.hasFixedSize()
                list_image.adapter = adapterNew
                adapterNew.onItemClick = { prof ->
                    val dataAllImage = prof[0] as DataAllImage
                    val pos = prof[1] as Int
                    val intent = Intent(this@MainActivity, PhotoActivity::class.java)
                    intent.putExtra("pos", user_id)
                    intent.putExtra("ids", dataAllImage.id)
                    intent.putExtra("path", dataAllImage.path)
                    intent.putExtra("date", dataAllImage.date)
                    intent.putExtra("day", dataAllImage.day)
                    startActivity(intent)
                   // showImage(prof)
                }
            }
        }
    }

    override fun onResume() {
        val status_del = SharedData.getKeyInt(this, "delete_status")
        if(status_del == 1){

            SharedData.setKeyInt(this, "delete_status", 0)
            recreate()
        }
        super.onResume()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId
        when (id) {
            R.id.action_favorite -> {
                //  showBottomSheet()
                if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                    drawer_layout.closeDrawer(Gravity.RIGHT)
                } else {
                    drawer_layout.openDrawer(Gravity.RIGHT)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun dpToPx(dp: Int): Int {

        val sdp = resources.getDimension(dp)

        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sdp,
            resources.displayMetrics
        ).toInt()
    }


    fun showImage(prof: Array<Any>) {
        val view = layoutInflater.inflate(R.layout.dialog_show_image, null)
        val dialog = BottomSheetDialog(this)
        val dataAllImage = prof[0] as DataAllImage
        val pos = prof[1] as Int
        val file = File(dataAllImage.path)
        view.txt_desc.text = dataAllImage.caption
        view.txt_date.text = dataAllImage.date
        view.txt_day.text = dataAllImage.day

        view.btn_delete.setOnClickListener {
            val dialog_popup = AlertDialog.Builder(this)

            dialog_popup.setTitle("Delete This Item ?")
                .setPositiveButton("YES") { dialogInterface: DialogInterface, i: Int ->
                    doAsync {
                        list_d = ArrayList()
                        val datas = SharedData.getKeyString(this@MainActivity, "list$user_id")!!
                        val favs2 = datas.replace("\\[|\\]| ".toRegex(), "").split(",".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        for (i in favs2.indices) {
                            list_d.add(Integer.parseInt(favs2[i]))
                        }
                        viewModel.deleteById(dataAllImage.id!!)
                        runOnUiThread {
                            list_d.remove(pos)
                            SharedData.setKeyString(this@MainActivity,"list$user_id", list_d.toString())
                            SharedData.setKeyInt(this@MainActivity,"number",0)
                            dialogInterface.dismiss()
                            dialog.dismiss()
                            toast("Delete Success")
                            recreate()
                        }
                    }
                }
                .setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            dialog_popup.show()
        }
        val imageUri = Uri.fromFile(file)
        Glide.with(this)
            .load(imageUri)
            .into(view.img_photo)
        dialog.setContentView(view)
        dialog.show()

    }


    fun showBottomSheet() {
        val view = layoutInflater.inflate(R.layout.fragment_item_list_dialog, null)
        val dialog = BottomSheetDialog(this)

        val adapters = AccountAdapter(list_user, R.layout.list_user)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        view.list_user.layoutManager = linearLayoutManager
        view.list_user.hasFixedSize()
        view.list_user.adapter = adapters
        if (user_id != -4) {
            adapters.id = user_id
        }
        dialog.setContentView(view)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        adapters.onItemClick = {
            adapters.id = it.id!!
            SharedData.setKeyInt(this, "user_id", it.id!!)
            SharedData.setKeyString(this, "name", it.name!!)
            SharedData.setKeyInt(this@MainActivity,"number",0)
            adapters.notifyDataSetChanged()
            dialog.dismiss()
            this.recreate()
            }
        view.btn_add.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode === Activity.RESULT_OK) {

                val dataString = data!!.dataString

                if (dataString != null) {
                    val a = Uri.parse(dataString)
                    val photoPath = getFilePath(this, a)

                    try {

                        // saveImage(photoPath)
                        dialogInsertImage(photoPath)

                    } catch (e: Exception) {

                        //     System.exit(0)
                    }

                }

            }

        }
        return

    }

    fun dialogInsertImage(photoPath: String?) {
        val view = layoutInflater.inflate(R.layout.dialog_insert_image, null)
        val dialog = BottomSheetDialog(this)
        val file = File(photoPath)
        val imageUri = Uri.fromFile(file)
        Glide.with(this)
            .load(imageUri)
            .into(view.img_photo)
        dialog.setContentView(view)
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).peekHeight =
                Resources.getSystem().getDisplayMetrics().heightPixels
        }

        dialog.show()

        view.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        view.btn_save.setOnClickListener {
            val caption = view.edt_caption.text.toString()
            saveImage(photoPath, dialog, caption)
        }
    }

    fun saveImage(
        photoPath: String?,
        dialog: BottomSheetDialog,
        caption: String
    ) {
        val b = decodeFile(photoPath!!)
        val byteArrayOutputStream = ByteArrayOutputStream()
        b!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = DataAllImage()
        data.path = photoPath
        data.caption = caption
        data.user_id = user_id
        data.image = getEncoded64ImageStringFromBitmap(b)
        data.position = list.size + 1
        data.date = dates
        data.day = day


        doAsync {
            viewModel.insert(data)
            runOnUiThread {
                //resetAdapter()
                var numb = SharedData.getKeyInt(this@MainActivity, "number")!!
                SharedData.setKeyInt(this@MainActivity,"number", numb++)
                dialog.dismiss()
                recreate()
                //getData()
                //  adapterNew.notifyItemInserted(adapterNew.list.size+1)
            }
        }

        //    getData()
    }


    @Throws(URISyntaxException::class)
    fun getFilePath(context: Context, uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                context.applicationContext,
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val envi = Environment.getExternalStorageDirectory()
                val cuk = (envi.toString() + "/" + split[1])
                return cuk
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor!!.moveToFirst()) {
                    return cursor!!.getString(column_index)
                }
            } catch (e: Exception) {
            }

        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    fun decodeFile(filePath: String): Bitmap? {

        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, o)

        // The new size we want to scale to
        val REQUIRED_SIZE = 1024

        // Find the correct scale value. It should be the power of 2.
        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        var image = BitmapFactory.decodeFile(filePath, o2)

        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)
            val exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            var rotate = 0
            when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90

                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180

                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            }

            if (rotate != 0) {
                val w = width_tmp / 5
                val h = height_tmp / 5

                // Setting pre rotate
                val mtx = Matrix()
                mtx.preRotate(rotate.toFloat())

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false)

            }
        } catch (e: IOException) {
            return null
        }

        return image.copy(Bitmap.Config.ARGB_8888, true)
    }


    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string

        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                //    viewFinder.post { startCamera(lensFacing) }
            } else {

            }
        }
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }


}


