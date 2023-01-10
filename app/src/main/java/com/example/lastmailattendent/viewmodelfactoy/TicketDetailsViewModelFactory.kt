package com.example.lastmailattendent.viewmodelfactoy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lastmailattendent.activityviewmodel.TicketDetailsViewModel
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider

class TicketDetailsViewModelFactory(
private val repository: Repository,
private val pref: PreferenceProvider

) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TicketDetailsViewModel(repository, pref) as T
    }
}