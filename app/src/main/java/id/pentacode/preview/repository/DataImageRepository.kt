package id.pentacode.preview.repository

import android.app.Application
import android.util.Log
import androidx.paging.DataSource
import id.pentacode.preview.Helper.Async
import id.pentacode.preview.db.db_preview
import id.pentacode.preview.db.entity.DataAllImage
import org.jetbrains.anko.doAsync

class DataImageRepository(application: Application) {
    val db = db_preview.getInstance(application)
    private val dataImageDao = db.DaoDataImage()
    val list = dataImageDao.getListUsers()


    fun insert(dataImage: DataAllImage) {
Async {
    dataImageDao.insert(dataImage)
}

    }

    fun update(dataImage: DataAllImage) {
        Async {
            dataImageDao.update(dataImage)
        }
    }

    fun updatePosition(newId: ArrayList<Int>, oldId: ArrayList<Int>){
        Async{
                dataImageDao.updateByPos(newId, oldId)
        }
    }

    fun delete(dataImage: DataAllImage) {
        Async {
            dataImageDao.delete(dataImage)

        }
    }
    fun getDataById(id: Int): DataAllImage{
        return dataImageDao.getDataById(id)
    }

    fun deleteById(id: Int) {

            dataImageDao.deleteById(id)

    }

    fun updateAll(list: List<DataAllImage>): Int {

         var a =   dataImageDao.updateAll(list)
        return  a
    }

    fun updateMultipleData(list: ArrayList<DataAllImage>){
        Async{
            dataImageDao.upsert(list)
        }
    }

//    fun updateById(id: Int, name: String, email: String){
//        Async{
//            dataImageDao.updateById(id, name, email)
//        }
//    }


    fun getAllsImage(userId: Int): List<Int> {
        var list = dataImageDao.getlistUsersAll(userId)
        return list
    }


}