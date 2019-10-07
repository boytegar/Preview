package id.pentacode.preview.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import id.pentacode.preview.db.entity.DataAllImage

@Dao
interface DaoDataImage{
    @Insert
    fun insert(dataImage: DataAllImage)
    @Update
    fun update(dataImage: List<DataAllImage>)
    @Delete
    fun delete(dataImage: DataAllImage)
    @Query("delete from data_image")
    fun deleteAll()
    @Query("select * from data_image")
    fun getListUsers(): DataSource.Factory<Int, DataAllImage>
    @Query("select * from data_image")
    fun getlistUsersAll(): List<DataAllImage>
    @Query("delete from data_image where id=:id")
    fun deleteById(id: Int)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(list: List<DataAllImage>)
//    @Query("update data_image set name=:name, email=:email where id=:id ")
//    fun updateById(id: Int, name: String, email: String)
}