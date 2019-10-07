package id.pentacode.preview

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.pentacode.preview.Helper.ItemTouchData
import id.pentacode.preview.Helper.OnStartDragListener
import id.pentacode.preview.db.entity.DataAllImage
import id.pentacode.preview.db.entity.DateImage
import id.pentacode.preview.viewmodel.DataImageViewModel
import id.pentacode.preview.viewmodel.DateImageViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnStartDragListener {


    private val INPUT_FILE_REQUEST_CODE = 1
    private var mCameraPhotoPath: String? = null
    private val REQUEST_CODE_PERMISSIONS = 10
 //   lateinit var list: ArrayList<DataAllImage>
    lateinit var itemtouch: ItemTouchHelper
    lateinit var adapterNew: FotoAdapterNew
    // This is an array of all the permission specified in the manifest
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    lateinit var list_date: List<DateImage>
    lateinit var viewModel: DataImageViewModel
    lateinit var viewModel2: DateImageViewModel
    lateinit var dates: String
    lateinit var day: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        getData()

    }

    override fun onStartDrag(viewHolder: FotoAdapterNew.PersonViewHolder) {
        itemtouch.startDrag(viewHolder)
    }


    override fun onResume() {
        getData()
        super.onResume()
    }

    fun getData() {

        doAsync {
            val list = viewModel.getAllImages() as ArrayList<DataAllImage>
            //   list_date = viewModel2.getPersonsLiveData()
            Log.e("SIZE LIST", list.toString())
            runOnUiThread {
                val adapter = FotoAdapterNew(
                    this@MainActivity,
                    R.layout.list_foto,
                    list,
                    viewModel,
                    this@MainActivity
                )

                val callback =
                    ItemTouchData(adapter)

                val linearLayoutManager = GridLayoutManager(this@MainActivity, 3)
                list_image.layoutManager = linearLayoutManager
                list_image.hasFixedSize()

                 itemtouch = ItemTouchHelper(callback)
                itemtouch.attachToRecyclerView(list_image)
                list_image.adapter = adapter
                adapterNew = adapter
                adapter.onItemClick = { prof ->


                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId
        when (id) {
            R.id.action_favorite -> {
                val view = layoutInflater.inflate(R.layout.fragment_item_list_dialog, null)
                val dialog = BottomSheetDialog(this)
                dialog.setContentView(view)
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

                        saveImage(photoPath)

                    } catch (e: Exception) {

                        //     System.exit(0)
                    }

                }

            }

        }
        return

    }

    fun saveImage(photoPath: String?) {
        val b = decodeFile(photoPath!!)
        val byteArrayOutputStream = ByteArrayOutputStream()
        b!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = DataAllImage()
        data.path = photoPath
        data.caption = ""
        data.group_id = 0
        data.image = getEncoded64ImageStringFromBitmap(b)

        val date = DateImage()
        date.time = dates
        date.day = day


        doAsync {
            viewModel.insert(data)
            //   viewModel2.insert(date)

            runOnUiThread {
                //list_image.recycledViewPool.clear()

                adapterNew.notifyDataSetChanged()
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
                val w = 100
                val h = 100

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
