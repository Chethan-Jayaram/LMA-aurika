package com.example.lastmailattendent.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.lastmailattendent.R
import com.example.lastmailattendent.activityviewmodel.EditProfileViewModel
import com.example.lastmailattendent.databinding.ActivityEditProfileBinding
import com.example.lastmailattendent.helper.EditProfileListner
import com.example.lastmailattendent.helper.GlobalClass
import com.example.lastmailattendent.helper.ScoketNotificationListner
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.responses.user.User
import com.example.lastmailattendent.util.hide
import com.example.lastmailattendent.util.show
import com.example.lastmailattendent.viewmodelfactoy.EditProfileViewModelFactory
import com.myhexaville.smartimagepicker.ImagePicker
import com.myhexaville.smartimagepicker.OnImagePickedListener
import kotlinx.android.synthetic.main.activity_edit_profile.*

import kotlinx.android.synthetic.main.layout_toolbar.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class EditProfileActivity : AppCompatActivity(), KodeinAware, EditProfileListner,
    ScoketNotificationListner {

    override val kodein by kodein()
    private val factory: EditProfileViewModelFactory by instance()
    private lateinit var profileModel: EditProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding
    private var imagePicker: ImagePicker? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        profileModel = ViewModelProvider(this, factory).get(EditProfileViewModel::class.java)

        iv_more.visibility = View.GONE
        profileModel.editProfileListner = this
        toolbar_title.setText("Edit Profile")



     /*   iv_profile.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_back
            )
        )*/
        imagePicker = ImagePicker(this, null,
            OnImagePickedListener { imageUri: Uri? ->
                img_profile.setImageURI(imageUri)
                profileModel.bitmap=(img_profile.drawable as BitmapDrawable ).bitmap
            })

        nav_menu.setImageResource(R.drawable.ic_back)
        nav_menu.setOnClickListener{
            onBackPressed()
        }
        profileModel.getProfileDetails()

     /*   iv_profile.setOnClickListener {
            onBackPressed()
        }*/

        img_profile.setOnClickListener {
            imagePicker!!.choosePicture(false)

        }
        Glide.with(this)
            .load(GlobalClass.url)
            .into(binding.imgProfile);
        binding.btnEditProfile.visibility=View.GONE;
    }

    override fun onResume() {
        super.onResume()
        HomeActivity.notificationListner=this
    }

    override fun onStarted() {
        binding.lytProfile.visibility = View.GONE
        profile_progress.show()
    }

    override fun onSuccess(user: User) {
       profileModel.mContactNumber=user.result.profile.contact_number
        profileModel.mEmail=user.result.email
        profileModel.mFirstName=user.result.first_name
        profileModel.mLastName=user.result.last_name
        profileModel.mprofileImage=user.result.profile.profile_image
        binding.viewModel=profileModel
        binding.user=user
        profile_progress.hide()
        binding.lytProfile.visibility = View.VISIBLE
    }

    override fun onProfileUpdatedSucessfully(message: String) {
        profile_progress.hide()
        binding.lytProfile.visibility = View.VISIBLE
       GlobalClass.ShowAlertDailog(this,"Alert",message)
    }

    override fun onFailure(message: String) {
        profile_progress.hide()
        GlobalClass.ShowAlertDailog(this, "Alert", message)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker!!.handleActivityResult(resultCode, requestCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker!!.handlePermission(requestCode, grantResults)
    }

    override fun OnNotificationListner(res: Data) {
        GlobalClass.notifcation(getWindow().getDecorView().getRootView() as ViewGroup,res!!,this)
    }

}