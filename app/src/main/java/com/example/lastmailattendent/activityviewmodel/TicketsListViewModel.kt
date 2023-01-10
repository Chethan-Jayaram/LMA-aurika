package com.example.lastmailattendent.activityviewmodel


import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.helper.TicketListListner
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


class TicketsListViewModel(
    private val repository: Repository,
    private val pref: PreferenceProvider
) : ViewModel() {




    var TicketListListner: TicketListListner? = null
    var ticketList = MutableLiveData<List<Data>>()
    var latestList: ArrayList<Data> = ArrayList()
    lateinit var mWebSocketClient: WebSocketClient



    val headerMap: HashMap<String?, String?>
    lateinit var mAcceptCompleteModel: HashMap<String?, String?>

    init {
        headerMap = HashMap()
        headerMap.put("Authorization", "bearer " + pref.getToken())
    }

    private val uri = URI("wss://exoneapi.aurikahotels.com:9002/ws/lmaticket/user-book-ticket/" + pref.getToken() + "/")

    fun getLatestTickets() {
        TicketListListner?.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getTicketList(headerMap)
                if (authResponse.status) {
                    latestList = authResponse.data as ArrayList<Data>
                    ticketList.postValue(latestList)
                    TicketListListner?.onSuccess(authResponse)
                    return@main
                } else {
                    TicketListListner?.onFailure(authResponse.message)
                }
            } catch (e: ApiException) {
                TicketListListner?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                TicketListListner?.onFailure(e.message!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




    fun connectWebSocket() {
        mWebSocketClient = object : WebSocketClient(uri) {

            override fun onOpen(serverHandshake: ServerHandshake) {

            }

            override fun onMessage(message: String) {
                try {
                    val gson = Gson()
                    val result: GeneralPojo = gson.fromJson(message, GeneralPojo::class.java)
                    TicketListListner?.onSocketTriggered(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                try {

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

            }
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


    fun onButtonClicked(v: View, result: Data) {
        try {
            mAcceptCompleteModel = HashMap()
            mAcceptCompleteModel.put("ticket_number", result.ticket_number)
            mAcceptCompleteModel.put("user", pref.getUser())
            if (result.assignee != null && result.current_status.event_name.equals(
                    "escalated",
                    ignoreCase = true
                )
            ) {
                TicketListListner!!.onEscaltedReason(1)
            } else if (result.assignee != null && result.current_status.event_name.equals(
                    "assigned",
                    ignoreCase = true
                )
            ) {
                v.isClickable=false
                mAcceptCompleteModel.put("note", "Completed On time")
                AcceptcompleteApiCall("user/booking-ticket-complete/")
            } else if (result.assignee == null && result.current_status.event_name.equals(
                    "escalated",
                    ignoreCase = true
                )
            ) {

                TicketListListner!!.onEscaltedReason(0)
            } /*else if (result.assignee == null && result.current_status.event_name.equals(
                    "new_ticket",
                    ignoreCase = true
                )
            ) {
                v.isClickable=false
                mAcceptCompleteModel.put("note", "Accepted on time")
                AcceptcompleteApiCall("booking-ticket-assignee/")
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //0 assigned, 1 completed
    fun getMessage(note: String, decider: Int) {
        if (decider == 0) {
          /*  if (!note.isEmpty()) {
                mAcceptCompleteModel.put("note", note)
                AcceptcompleteApiCall("booking-ticket-assignee/")
                // startTimer()
            }*/
        } else if (decider == 1) {
            if (!note.isEmpty()) {
                mAcceptCompleteModel.put("note", note)
                AcceptcompleteApiCall("user/booking-ticket-complete/")
                //startTimer()
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
                        TicketListListner?.onFailure(authResponse.error.toString())
                    }
                } catch (e: ApiException) {
                    TicketListListner?.onFailure(e.message!!)
                } catch (e: NoInternetException) {
                    TicketListListner?.onFailure(e.message!!)
                }
            }
        }
    }


    /* private fun startTimer(view:LinearLayout, initialtime: Long, position: Int) {
        timer= object : CountDownTimer(initialtime, 1000) {
             @SuppressLint("SetTextI18n")
             override fun onTick(millisUntilFinished: Long) {
             tv_Timer.setText(
                     "" + TimeUnit.MILLISECONDS.toHours(millisUntilFinished) + ":"
                             + (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60) +
                             ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)
                 )

             }
             override fun onFinish() {
                 result.istriggered=false
               timerProgressbar.setProgress(100)
             }
         }
         timer?.start()
     }
 */

}