package id.pentacode.preview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import id.pentacode.preview.db.entity.User
import id.pentacode.preview.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_add_user.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*

class AddUserActivity : AppCompatActivity() {

    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        setSupportActionBar(toolbar2)
        title ="Add User"
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        button.setOnClickListener {
            val user = User()

            val name = edt_user.text.toString()
            user.name = name
            val desc = edt_desc.text.toString()
            user.desc = desc
            viewModel.insert(user)
            finish()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
