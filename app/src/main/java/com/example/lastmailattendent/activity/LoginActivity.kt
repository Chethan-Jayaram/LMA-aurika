package com.example.lastmailattendent.activity


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lastmailattendent.R
import com.example.lastmailattendent.activityviewmodel.LoginViewModel
import com.example.lastmailattendent.databinding.ActivityMainBinding
import com.example.lastmailattendent.helper.AuthListener
import com.example.lastmailattendent.helper.GlobalClass
import com.example.lastmailattendent.responses.login.LoginResponse
import com.example.lastmailattendent.viewmodelfactoy.LoginViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity :AppCompatActivity() , AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory : LoginViewModelFactory by instance()
    private var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progress = ProgressDialog(this)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val loginModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        binding.loginModel = loginModel
        loginModel.authListener = this
    }

    override fun onStarted() {
        progress!!.setTitle("Loading....!!")
        progress!!.setMessage("Please wait while we verify your credentials")
        progress!!.setCancelable(false)
        progress!!.show()
    }

    override fun onSuccess(response: LoginResponse) {

        GlobalClass.url=response.data.result.profile.profile_image
        GlobalClass.mUserName=response.data.result.first_name
        GlobalClass.userID=response.data.result.id
        GlobalClass.employeID=response.data.result.profile.employee_code
          Intent(this, HomeActivity::class.java).also {
          startActivity(it)
            finish()
        }


        progress!!.dismiss()
    }


    override fun onFailure(message: String) {
       // toast(message)
        progress!!.dismiss()
        GlobalClass.ShowAlertDailog(this,"Alert",message)
    }

}
