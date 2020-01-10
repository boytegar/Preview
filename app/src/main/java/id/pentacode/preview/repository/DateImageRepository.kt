package id.pentacode.preview.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import id.pentacode.preview.Helper.Async
import id.pentacode.preview.db.db_preview
import id.pentacode.preview.db.entity.DateImage

class DateImageRepository(application: Application) {
    val db = db_preview.getInstance(application)
    private val dataImageDao = db.DaoDateImage()

    fun insert(dateImage: DateImage) {

            dataImageDao.insert(dateImage)
        Log.e("INSERT IN","GOOOOOOOO")
    }

    fun update(dateImage: DateImage) {
        Async {
            dataImageDao.update(dateImage)
        }
    }

    fun delete(dateImage: DateImage) {
        Async {
            dataImageDao.delete(dateImage)
        }
    }

    fun deleteById(id: Int) {

            dataImageDao.deleteById(id)

    }

//    fun updateById(id: Int, name: String, email: String){
//        Async{
//            dataImageDao.updateById(id, name, email)
//        }
//    }

    fun getAllDate(user_id: Int): List<DateImage> {
        var list: List<DateImage> = dataImageDao.getListUsers(user_id)
        return list
    }

}