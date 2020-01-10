package id.pentacode.preview.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import id.pentacode.preview.db.entity.DateImage


@Dao
interface DaoDateImage{
    @Insert
    fun insert(dateImage: DateImage)
    @Update
    fun update(dateImage: DateImage)
    @Delete
    fun delete(dateImage: DateImage)
    @Query("delete from date_image")
    fun deleteAll()
    @Query("select * from date_image where user_id=:user_id order by id desc")
    fun getListUsers(user_id: Int): List<DateImage>
    @Query("delete from date_image where id=:id")
    fun  deleteById(id: Int)
//    @Query("update data_image set name=:name, email=:email where id=:id ")
//    fun updateById(id: Int, name: String, email: String)
}