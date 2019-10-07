package id.pentacode.preview.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "date_image")
class DateImage() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var day: String = ""
    var time: String = ""

    @Ignore
    constructor(day:String, time: String): this() {
        this.day = day
        this.time = time

    }
}