package id.pentacode.preview.db.entity


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "data_image")
class DataAllImage() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var caption: String = ""
    var image: String = ""
    var path: String = ""
    var group_id: Int = 0

    @Ignore
    constructor(caption:String, image: String, path: String, group_id: Int): this() {
        this.caption = caption
        this.image = image
        this.path = path
        this.group_id = group_id
    }
}