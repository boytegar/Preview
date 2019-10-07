package id.pentacode.preview.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import id.pentacode.preview.Helper.Async
import id.pentacode.preview.db.db_preview
import id.pentacode.preview.db.entity.DataAllImage

class DataImageRepository(application: Application) {
    val db = db_preview.getInstance(application)
    private val dataImageDao = db.DaoDataImage()
    val list = dataImageDao.getListUsers()


    fun insert(dataImage: DataAllImage){

            dataImageDao.insert(dataImage)

    }
    fun update(dataImage:  List<DataAllImage>){
        Async{
            dataImageDao.update(dataImage)
        }
    }
    fun delete(dataImage: DataAllImage){
        Async{
            dataImageDao.delete(dataImage)

        }


    }
    fun deleteById(id: Int){
        Async{
            dataImageDao.deleteById(id)
        }
    }

    fun updateAll(list: List<DataAllImage>){
        Async{
            dataImageDao.updateAll(list)
        }

    }

//    fun updateById(id: Int, name: String, email: String){
//        Async{
//            dataImageDao.updateById(id, name, email)
//        }
//    }

    fun getAllImage(): DataSource.Factory<Int, DataAllImage>{
        return list
    }

    fun getAllsImage():List<DataAllImage>{
        return dataImageDao.getlistUsersAll()
    }



}