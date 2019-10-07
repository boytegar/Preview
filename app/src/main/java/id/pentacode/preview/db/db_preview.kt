package id.pentacode.preview.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import id.pentacode.preview.db.dao.DaoDataImage
import id.pentacode.preview.db.dao.DaoDateImage
import id.pentacode.preview.db.entity.DataAllImage
import id.pentacode.preview.db.entity.DateImage

@Database(entities = [(DataAllImage::class), (DateImage::class)], version = 1)
abstract class db_preview: RoomDatabase() {

    abstract fun DaoDataImage(): DaoDataImage
    abstract fun DaoDateImage(): DaoDateImage

    companion object {
        private var instance: db_preview? = null
        @Synchronized
        fun getInstance(context: Context): db_preview {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    db_preview::class.java!!, "db_preview")
                    .fallbackToDestructiveMigration()
                    // .addCallback(roomCallback)
                    .build()
            }
            return instance as db_preview
        }


        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //PopulateDbAsyncTask(instance).execute()
            }

        }
    }

}