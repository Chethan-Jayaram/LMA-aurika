package com.example.lastmailattendent.activityviewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.lastmailattendent.helper.AuthListener
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.util.ApiException
import com.example.lastmailattendent.util.Coroutines
import com.example.lastmailattendent.util.NoInternetException
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider
import com.onesignal.OneSignal
import java.util.*

class LoginViewModel(
    private val repository: Repository,
    private val pref: PreferenceProvider
): ViewModel() {

    var name: String? = null
    var password: String? = null

    var authListener: AuthListener? = null


    fun onSignInButtonClick(view: View) {
        view.isClickable=false
        authListener?.onStarted()
        if (name.isNullOrEmpty()) {
            view.isClickable=true
            authListener?.onFailure("Name is required")
            return
        } else if (password.isNullOrEmpty()) {
            view.isClickable=true
            authListener?.onFailure("Please enter a password")
            return
        }

        val headerMap: HashMap<String?, String?> = HashMap()
        headerMap.put("username", name!!)
        headerMap.put("password", password!!)
        headerMap.put("player_id",  OneSignal.getDeviceState()?.userId)

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(headerMap)
                if(authResponse.status){
                    authListener?.onSuccess(authResponse)
                    pref.setToken(authResponse.data.token, authResponse.data.result.id.toString())
                    return@main
                }else{
                    view.isClickable=true
                    authListener?.onFailure(authResponse.message)
                }
            }catch (e: ApiException){
                view.isClickable=true
                authListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                view.isClickable=true
                authListener?.onFailure(e.message!!)
            }catch (e: Exception){
                authListener?.onFailure("")
                view.isClickable=true
                e.printStackTrace()
            }
        }
    }
}