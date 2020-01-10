package id.pentacode.preview.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User() {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String = ""
    var desc: String = ""

    @Ignore
    constructor(name:String, desc: String): this() {
        this.name = name
        this.desc = desc

    }
}