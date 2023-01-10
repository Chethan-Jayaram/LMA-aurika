package com.example.lastmailattendent.helper


import com.example.lastmailattendent.responses.login.LoginResponse
import com.example.lastmailattendent.responses.GeneralPojo
import com.example.lastmailattendent.responses.TicketDetails
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.responses.ticketlist.TicketList
import com.example.lastmailattendent.responses.user.User


interface AuthListener {
    fun onStarted()
    fun onSuccess(loginResponse: LoginResponse)
    fun onFailure(message: String)

}

interface TicketListListner {
    fun onStarted()
    fun onSuccess(ticketList: TicketList)
    fun onFailure(message: String)
    fun onEscaltedReason( str:Int)

    fun onSocketTriggered(result: GeneralPojo)


}



interface ScoketNotificationListner{
    fun OnNotificationListner(res:Data)
}


interface TicketDetailsListner {
    fun onStarted()
    fun onSuccess(ticketList:TicketDetails)
    fun onFailure(message: String)
    fun onEscaltedReason(str:Int)
}

interface OnclickListner{
    fun onItemClicked(pos:Int)
}

interface EditProfileListner{
    fun onStarted()
    fun onSuccess(user:User)
    fun onProfileUpdatedSucessfully(message:String)
    fun onFailure(message: String)
}



