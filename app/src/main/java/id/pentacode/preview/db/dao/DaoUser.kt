package id.pentacode.preview.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import id.pentacode.preview.db.entity.User

@Dao
interface DaoUser{
    @Insert
    fun insert(user: User)
    @Update
    fun update(user: User)
    @Delete
    fun delete(user: User)
    @Query("delete from user")
    fun deleteAll()
    @Query("select * from user")
    fun getListUsers(): LiveData<List<User>>
    @Query("delete from user where id=:id")
    fun  deleteById(id: Int)
//    @Query("update data_image set name=:name, email=:email where id=:id ")
//    fun updateById(id: Int, name: String, email: String)
}