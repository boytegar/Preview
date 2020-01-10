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
    var user_id: Int = 0

    @Ignore
    constructor(day: String, time: String, user_id: Int) : this() {
        this.day = day
        this.time = time
        this.user_id = user_id

    }
}