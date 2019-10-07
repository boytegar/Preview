package id.pentacode.preview.Helper

import android.os.AsyncTask


class Async(val handler: () -> Unit) : AsyncTask<String, String, String>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: String?): String? {
        handler()
        return null
    }

    override fun onPostExecute(result: String?) {
        //handler()
        super.onPostExecute(result)
    }

}