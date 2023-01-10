package com.example.lastmailattendent.helper



import com.example.lastmailattendent.responses.AcceptComplete
import com.example.lastmailattendent.responses.TicketDetails
import com.example.lastmailattendent.responses.login.LoginResponse
import com.example.lastmailattendent.responses.ticketlist.TicketList
import com.example.lastmailattendent.responses.updateprofile.ProfileUpdate
import com.example.lastmailattendent.responses.user.User
import com.example.lastmailattendent.util.data.network.MyApi
import com.example.lastmailattendent.util.data.network.SafeApiRequest
import kotlin.collections.HashMap

class Repository(
    private val api: MyApi
) : SafeApiRequest() {

    suspend fun userLogin(headerMap: HashMap<String?, String?>): LoginResponse {
        return apiRequest { api.userLogin(headerMap) }
    }

    suspend fun getTicketList(headerMap: HashMap<String?, String?>): TicketList {
        return apiRequest { api.getTicketList(headerMap) }
    }


    suspend fun getTicketDetails(headerMap: HashMap<String?, String?>, id: Int): TicketDetails {
        return apiRequest { api.getTicketDetails(headerMap, id) }
    }


    suspend fun postAcceptTkt(headerMap: HashMap<String?, String?>, mAcceptCompleteModel: HashMap<String?, String?>,url:String): AcceptComplete {
        return apiRequest { api.postAcceptTkt(headerMap, mAcceptCompleteModel,url) }
    }

    suspend fun getProfileDetails(headerMap: HashMap<String?, String?>, int: Int): User {
        return apiRequest { api.getProfileDetails(headerMap,int) }
    }

    suspend fun postProfileDetails(headerMap: HashMap<String?, String?>, int: Int,profiledetails:HashMap<String?,Any?>): ProfileUpdate {
        return apiRequest { api.postProfileDetails(headerMap,int,profiledetails) }
    }


}