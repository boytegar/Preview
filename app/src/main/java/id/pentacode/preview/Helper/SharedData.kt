package id.pentacode.preview.Helper

import android.content.Context
import android.content.SharedPreferences

class SharedData {
    companion object {
        private val MyPREFERENCES = "MyPrefs"
        private var sharedPreferences: SharedPreferences? = null
        fun getKeyString(c: Context, key: String): String? {
            sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
            return sharedPreferences!!.getString(key, "")
        }

        fun setKeyString(c: Context, key: String, value: String) {
            sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences!!.edit()
            editor.putString(key, value)
            editor.commit()
        }
        fun getKeyInt(c: Context, key: String): Int? {
            sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
            return sharedPreferences!!.getInt(key, 0)
        }

        fun setKeyInt(c: Context, key: String, value: Int) {
            sharedPreferences = c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences!!.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        fun clear(c: Context){
            c.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit().clear().apply()
        }


    }
}