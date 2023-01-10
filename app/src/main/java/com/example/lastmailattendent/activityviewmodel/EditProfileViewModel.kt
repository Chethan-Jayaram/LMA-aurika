package com.example.lastmailattendent.activityviewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import com.example.lastmailattendent.helper.EditProfileListner
import com.example.lastmailattendent.helper.GlobalClass
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.responses.user.User
import com.example.lastmailattendent.util.ApiException
import com.example.lastmailattendent.util.Coroutines
import com.example.lastmailattendent.util.NoInternetException
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider
import kotlinx.android.synthetic.main.activity_edit_profile.view.*

class EditProfileViewModel(
   private  val repository: Repository,
  private val pref: PreferenceProvider
) :ViewModel() {

    var editProfileListner: EditProfileListner?=null

    val headerMap: HashMap<String?, String?>
    var bitmap: Bitmap? = null
    lateinit var mUserDetails: HashMap<String?, Any?>
    lateinit var mProfileDetails: HashMap<String?, String?>



    var mFirstName=""
    var mLastName=""
    var mContactNumber=""
    var mEmail=""
    var mprofileImage=""


    init {
        headerMap = HashMap()
        headerMap.put("Authorization", "bearer " + pref.getToken())
    }

    fun getProfileDetails() {
        editProfileListner?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getProfileDetails(headerMap,pref.getUser()!!.toInt())
                if(authResponse.status=="Success"){
                    editProfileListner?.onSuccess(authResponse)
                    return@main
                }else{
                    editProfileListner?.onFailure(authResponse.error)
                }
            }catch(e: ApiException){
                editProfileListner?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                editProfileListner?.onFailure(e.message!!)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun onUpdateButtonClick(user: User){
        try {
            var img_base64_st: String? = ""
            if (mFirstName.isEmpty()) {
                editProfileListner?.onFailure("First Name is required")
                return
            } else if (mLastName.isEmpty()) {
                editProfileListner?.onFailure("Last Name is required")
                return
            } else if (mContactNumber.isEmpty()) {
                editProfileListner?.onFailure("Mobile Number is required")
                return
            } else if (mEmail.isEmpty()) {
                editProfileListner?.onFailure("Email is required")
                return
            }
            mProfileDetails = HashMap()
            mUserDetails = HashMap()
            bitmap?.let {
                img_base64_st = GlobalClass.encodeTobase64(bitmap!!)
                mProfileDetails.put("profile_image", "data:image/jpeg;base64," + img_base64_st)
            }

            mUserDetails.put("first_name", mFirstName)
            mUserDetails.put("last_name", mLastName)
            mUserDetails.put("username", user.result.username)
            mUserDetails.put("password", user.result.password)
            mUserDetails.put("groups",user.result.groups.id)

            mProfileDetails.put("contact_number", mContactNumber)
            mProfileDetails.put("employee_code", user.result.profile.employee_code)

            mUserDetails.put("profile", mProfileDetails)

            editProfileListner?.onStarted()
            Coroutines.main {
                try {
                    val authResponse = repository.postProfileDetails(
                        headerMap,
                        pref.getUser()!!.toInt(),
                        mUserDetails
                    )
                    if (authResponse.status == "Success") {
                        GlobalClass.url=authResponse.result.profile.profile_image
                        GlobalClass.mUserName=authResponse.result.first_name
                        editProfileListner?.onProfileUpdatedSucessfully("Profile Updated Successfully")
                        return@main
                    } else {
                        editProfileListner?.onFailure(authResponse.error)
                    }
                } catch (e: ApiException) {
                    editProfileListner?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    editProfileListner?.onFailure(e.message!!)
                } catch (e: Exception) {
                    editProfileListner?.onFailure(e.message!!)
                    e.printStackTrace()
                }
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }

}