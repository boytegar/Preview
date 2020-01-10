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
    fun update(dataImage: DataAllImage)
    @Delete
    fun delete(dataImage: DataAllImage)
    @Query("delete from data_image")
    fun deleteAll()
    @Query("select * from data_image")
    fun getListUsers(): DataSource.Factory<Int, DataAllImage>

    @Query("select id from data_image where user_id=:user_id order by id desc")
    fun getlistUsersAll(user_id: Int): List<Int>
    @Query("delete from data_image where id=:id")
    fun deleteById(id: Int)

    @Query("select * from data_image where id=:id")
    fun getDataById(id: Int): DataAllImage


    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateAll(list: List<DataAllImage>): Int

    @Update
    fun updateMutipleData(vararg dataImage: DataAllImage)

    @Query("UPDATE data_image set position=:newid WHERE position=:oldId")
    fun updatePosition(newid: Int, oldId: Int)


    @Transaction
    fun upsert(weeks: List<DataAllImage>) {
        updateAll(weeks)
    }

    @Transaction
    fun updateByPos(list_new: List<Int>, list_old: List<Int>) {
        for (i in list_new.indices) {
            updatePosition(list_new[i], list_old[i])
        }
    }

}