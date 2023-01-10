package com.example.lastmailattendent.viewmodelfactoy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lastmailattendent.activityviewmodel.EditProfileViewModel
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider

class EditProfileViewModelFactory   (private val repository: Repository,
                                     private val pref: PreferenceProvider
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileViewModel(repository, pref) as T
    }
}