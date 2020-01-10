package id.pentacode.preview.repository

import android.app.Application
import androidx.lifecycle.LiveData
import id.pentacode.preview.Helper.Async
import id.pentacode.preview.db.db_preview
import id.pentacode.preview.db.entity.User

class UserRepository(application: Application) {
    val db = db_preview.getInstance(application)
    private val userDao = db.DaoUser()
    lateinit var list: LiveData<List<User>>

    init {
        Async {
            list = userDao.getListUsers()
        }
    }

    fun insert(user: User) {
        Async {
            userDao.insert(user)
        }

    }

    fun update(user: User) {
        Async {
            userDao.update(user)
        }
    }

    fun delete(user: User) {
        Async {
            userDao.delete(user)
        }
    }

    fun deleteById(id: Int) {
        Async {
            userDao.deleteById(id)
        }
    }

//    fun updateById(id: Int, name: String, email: String){
//        Async{
//            dataImageDao.updateById(id, name, email)
//        }
//    }

    fun getAllUser(): LiveData<List<User>> {
        return list
    }

}