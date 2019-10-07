package id.pentacode.preview.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.pentacode.preview.db.entity.DateImage
import id.pentacode.preview.repository.DateImageRepository

class DateImageViewModel (application: Application): AndroidViewModel(application){
    private val dateImageRepository = DateImageRepository(application)

    fun getPersonsLiveData():List<DateImage> {
       return dateImageRepository.getAllImage()
    }
    fun insert(dataImage: DateImage){
        dateImageRepository.insert(dataImage)
    }
    fun update(dataImage: DateImage){
        dateImageRepository.insert(dataImage)
    }
    fun delete(dataImage: DateImage){
        dateImageRepository.delete(dataImage)
    }
    fun  deleteById(id: Int){
        dateImageRepository.deleteById(id)
    }
//    fun updateById(id: Int, name: String, email: String){
//        dataImageRepository.updateById(id, name, email)
//    }

}