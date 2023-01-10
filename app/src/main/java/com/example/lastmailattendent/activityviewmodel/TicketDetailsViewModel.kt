package com.example.lastmailattendent.activityviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.helper.TicketDetailsListner
import com.example.lastmailattendent.responses.GeneralPojo
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.util.ApiException
import com.example.lastmailattendent.util.Coroutines
import com.example.lastmailattendent.util.NoInternetException
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class TicketDetailsViewModel(private val repository: Repository,
                             private val pref: PreferenceProvider
) : ViewModel()  {


 var TicketDetailListner: TicketDetailsListner? = null
    var ticketDetails = MutableLiveData<Data>()
    var latestresult:Data?=null
    var id:Int?=null
    lateinit var mWebSocketClient: WebSocketClient


    val headerMap: HashMap<String?, String?>
    lateinit var mAcceptCompleteModel: HashMap<String?, String?>
    init {
        headerMap = HashMap()
        headerMap.put("Authorization", "bearer " + pref.getToken())
    }

    private val uri = URI("wss://dev.mobisprint.com:8007/ws/bookticket/book-ticket/" + pref.getToken() + "/")


    fun getTicketDetails(id:Int) {
        this.id=id
        TicketDetailListner?.onStarted()
        val headerMap: HashMap<String?, String?> = HashMap()
        headerMap.put("Authorization", "bearer " + pref.getToken())
        Coroutines.main {
            try {
                val authResponse = repository.getTicketDetails(headerMap,id)
                if(authResponse.status){
                    latestresult=authResponse.data
                    ticketDetails.postValue(latestresult)
                    TicketDetailListner?.onSuccess(authResponse)
                    return@main
                }else{
                    TicketDetailListner?.onFailure(authResponse.message)
                }
            }catch(e: ApiException){
                TicketDetailListner?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                TicketDetailListner?.onFailure(e.message!!)
            }
        }
    }


    fun connectWebSocket() {
        mWebSocketClient = object : WebSocketClient(uri) {

            override fun onOpen(serverHandshake: ServerHandshake) {
            }


            override fun onMessage(message: String) {


                val gson = Gson()
                val result: GeneralPojo = gson.fromJson(message, GeneralPojo::class.java)

                if(id==result.message.id){
                    latestresult=result.message
                    ticketDetails.postValue(latestresult)
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {}
            override fun onError(e: Exception) {
                try {
                    connectWebSocket()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        mWebSocketClient.connect()
    }

    fun onButtonClicked( result: Data) {
        try {
            mAcceptCompleteModel = HashMap()
            mAcceptCompleteModel.put("ticket_number", result.ticket_number)
            mAcceptCompleteModel.put("user", pref.getUser())
            if (result.assignee != null && result.current_status.event_name.equals(
                    "escalated",
                    ignoreCase = true
                )
            ) {
                TicketDetailListner!!.onEscaltedReason(1)
            } else if (result.assignee != null && result.current_status.event_name.equals(
                    "assigned",
                    ignoreCase = true
                )
            ) {
                mAcceptCompleteModel.put("note", "Completed On time")
                AcceptcompleteApiCall("booking-ticket-complete/")
            } else if (result.assignee == null && result.current_status.event_name.equals(
                    "escalated",
                    ignoreCase = true
                )
            ) {
                TicketDetailListner!!.onEscaltedReason(0)
            } else if (result.assignee == null && result.current_status.event_name.equals(
                    "new_ticket",
                    ignoreCase = true
                )
            ) {
                mAcceptCompleteModel.put("note", "Accepted on time")
                AcceptcompleteApiCall("booking-ticket-assignee/")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //0 assigned, 1 completed
    fun getMessage(note: String, decider: Int) {
        if (decider == 0) {
            if (!note.isEmpty()) {
                mAcceptCompleteModel.put("note", note)
                AcceptcompleteApiCall("booking-ticket-assignee/")
            }
        } else if (decider == 1) {
            if (!note.isEmpty()) {
                mAcceptCompleteModel.put("note", note)
                AcceptcompleteApiCall("booking-ticket-complete/")
            }
        }
    }


    private fun AcceptcompleteApiCall(url: String) {
        if (!url.isEmpty()) {
            Coroutines.main {
                try {
                    val authResponse =
                        repository.postAcceptTkt(headerMap, mAcceptCompleteModel, url)
                    if (authResponse.status == "Success") {
                        return@main
                    } else {
                        TicketDetailListner?.onFailure(authResponse.error.toString())
                    }
                } catch (e: ApiException) {
                    TicketDetailListner?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    TicketDetailListner?.onFailure(e.message!!)
                }
            }
        }
    }







}