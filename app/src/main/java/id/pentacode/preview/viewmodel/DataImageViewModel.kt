package id.pentacode.preview.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.pentacode.preview.db.entity.DataAllImage
import id.pentacode.preview.repository.DataImageRepository

class DataImageViewModel (application: Application): AndroidViewModel(application){
    private val dataImageRepository = DataImageRepository(application)

    private var personsLiveData: LiveData<PagedList<DataAllImage>>
    init {
        val factory: DataSource.Factory<Int, DataAllImage> = dataImageRepository.getAllImage()

        val pagedListBuilder: LivePagedListBuilder<Int, DataAllImage> = LivePagedListBuilder(factory,
            20)
        personsLiveData = pagedListBuilder.build()
    }
    fun getPersonsLiveData() = personsLiveData

    fun insert(dataImage: DataAllImage){
        dataImageRepository.insert(dataImage)
    }
    fun update(dataImage: DataAllImage){
        dataImageRepository.insert(dataImage)
    }
    fun delete(dataImage: DataAllImage){
        dataImageRepository.delete(dataImage)
    }
    fun  deleteById(id: Int){
        dataImageRepository.deleteById(id)
    }
    fun updateAll(list: List<DataAllImage>){

        dataImageRepository.updateAll(list)
    }

    fun getAllImages(): List<DataAllImage>{
        return dataImageRepository.getAllsImage()
    }

//    fun updateById(id: Int, name: String, email: String){
//        dataImageRepository.updateById(id, name, email)
//    }

}