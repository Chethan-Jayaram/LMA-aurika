package com.example.lastmailattendent.unused

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider

class TicketsDataSourceFactory(private val pref: PreferenceProvider,
                               private val repository: Repository

) : DataSource.Factory<Int, Data>() {
    val userLiveDataSource = MutableLiveData<TicketsDataSource>()
    override fun create(): DataSource<Int, Data> {
        val userDataSource =
            TicketsDataSource(
                pref,
                repository
            )
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }

  /*
    fun updateData(result: Result){

        userLiveDataSource.postValue()
    }*/





}