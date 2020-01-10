package id.pentacode.preview.db.entity


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.FieldPosition

@Entity(tableName = "data_image")
class DataAllImage() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var caption: String = ""
    var image: String = ""
    var path: String = ""
    var user_id: Int = 0
    var position: Int = 0
    var date: String = ""
    var day: String = ""

    @Ignore
    constructor(caption:String, image: String, path: String, user_id: Int, position: Int, date: String, day: String): this() {
        this.caption = caption
        this.image = image
        this.path = path
        this.user_id = user_id
        this.position = position
        this.date = date
        this.day = day
    }
}