package id.pentacode.preview.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import id.pentacode.preview.db.entity.User
import id.pentacode.preview.repository.UserRepository

class UserViewModel (application: Application): AndroidViewModel(application){
    private val UserRepository = UserRepository(application)

//    fun getPersonsLiveData():List<User> {
//        return UserRepository.getAllUser()
//    }
    fun insert(user: User){
        UserRepository.insert(user)
    }
    fun update(user: User){
        UserRepository.insert(user)
    }
    fun delete(user: User){
        UserRepository.delete(user)
    }
    fun  deleteById(id: Int){
        UserRepository.deleteById(id)
    }
//    fun updateById(id: Int, name: String, email: String){
//        dataImageRepository.updateById(id, name, email)
//    }

}