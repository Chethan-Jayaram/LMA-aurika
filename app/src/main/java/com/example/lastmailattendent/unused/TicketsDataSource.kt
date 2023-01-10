package com.example.lastmailattendent.unused

import androidx.paging.PageKeyedDataSource
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider

class TicketsDataSource(private val pref:PreferenceProvider,
                        private val repository: Repository ) : PageKeyedDataSource<Int, Data>() {


    var url: String = "list-ticket/"
    val headerMap: HashMap<String?, String?> = HashMap()

    init {
        headerMap.put("Authorization", "bearer " + pref.getToken())
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Data>) {

    /*    Coroutines.main {
            try {
                val authResponse = repository.getTicketList(headerMap,url)
                if(authResponse.status=="Success"){
                    var key:Int?=null
                    authResponse?.let {
                        authResponse.next?.let{
                            url=authResponse.next
                            key= FIRST_PAGE +1
                        }
                        Log.d("response",authResponse.result.toString())
                        callback.onResult(authResponse.result, null, key)
                    }
                    return@main
                }
            }catch(e: ApiException){
                e.printStackTrace()
            }catch (e: NoInternetException){
                e.printStackTrace()
            }
        }*/
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {

       /* Coroutines.main {
            try {
                val authResponse = repository.getTicketList(headerMap,url)
                if(authResponse.status=="Success"){
                    val key = if (params.key > 1) params.key - 1 else 0
                    authResponse?.let {
                        try {
                            authResponse.previous?.let {
                                url = authResponse.previous as String
                            }
                            callback.onResult(authResponse.result, key)

                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }
                    return@main
                }
            }catch(e: ApiException){
                e.printStackTrace()
            }catch (e: NoInternetException){
                e.printStackTrace()
            }
        }*/





     /*   val service = ApiServiceBuilder.buildService(ApiService::class.java)
        val call = service.getUsers(params.key)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()!!
                    val responseItems = apiResponse.users
                    val key = if (params.key > 1) params.key - 1 else 0
                    responseItems?.let {
                        callback.onResult(responseItems, key)
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            }
        })*/
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Data>) {

      /*  Coroutines.main {
            try {
                val authResponse = repository.getTicketList(headerMap,url)
                if(authResponse.status=="Success"){
                    try {
                        var key:Int?=null
                        authResponse?.let {
                            authResponse.next?.let {
                                url = authResponse.next
                                key = params.key + 1
                            }
                            callback.onResult(authResponse.result, key)
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    return@main
                }
            }catch(e: ApiException){
                e.printStackTrace()
            }catch (e: NoInternetException){
                e.printStackTrace()
            }
        }*/

    }
    companion object {
        const val PAGE_SIZE = 15
        const val FIRST_PAGE = 1
    }
}