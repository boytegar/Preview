package id.pentacode.preview.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.pentacode.preview.db.entity.DataAllImage
import id.pentacode.preview.db.entity.DateImage
import id.pentacode.preview.db.entity.User
import id.pentacode.preview.repository.DataImageRepository
import id.pentacode.preview.repository.DateImageRepository
import id.pentacode.preview.repository.UserRepository

class DataImageViewModel (application: Application): AndroidViewModel(application){
    private val dataImageRepository = DataImageRepository(application)
    val userRepository = UserRepository(application)
    val dateImageRepository = DateImageRepository(application)
    val list_id = MutableLiveData<ArrayList<Int>>()

//    private var personsLiveData: LiveData<PagedList<DataAllImage>>
//    init {
//        val factory: DataSource.Factory<Int, DataAllImage> = dataImageRepository.getAllImage()
//
//        val pagedListBuilder: LivePagedListBuilder<Int, DataAllImage> = LivePagedListBuilder(factory,
//            20)
//        personsLiveData = pagedListBuilder.build()
//    }
//    fun getPersonsLiveData() = personsLiveData

    fun insert(dataImage: DataAllImage){
        dataImageRepository.insert(dataImage)
    }
    fun update(dataImage: DataAllImage){
        dataImageRepository.update(dataImage)
    }
    fun delete(dataImage: DataAllImage){
        dataImageRepository.delete(dataImage)
    }

    fun updatePosition(newId: ArrayList<Int>, oldId: ArrayList<Int>){
        dataImageRepository.updatePosition(newId, oldId)
    }

    fun  deleteById(id: Int){
        dataImageRepository.deleteById(id)
    }
    fun deleteByIdDate(id: Int){
        dateImageRepository.deleteById(id)
    }
    fun updateAll(list: List<DataAllImage>): Int{

       val a = dataImageRepository.updateAll(list)
        return  a
    }
    fun updateMultipleData(list: ArrayList<DataAllImage>){
        dataImageRepository.updateMultipleData(list)
    }

    fun getDatabyId(id: Int): DataAllImage{
       return dataImageRepository.getDataById(id)
    }

    fun insertDate(dateImage: DateImage){
        dateImageRepository.insert(dateImage)
    }

    fun getAllImages(user_id : Int): List<Int>{
        return dataImageRepository.getAllsImage(user_id)
    }

    fun getAllDate(user_id : Int): List<DateImage>{
        return dateImageRepository.getAllDate(user_id)
    }

    fun getAllUsers(): LiveData<List<User>> {
        val us = userRepository.getAllUser()
        return us
    }

}