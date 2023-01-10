package com.example.lastmailattendent.util.data.network


import com.example.lastmailattendent.responses.AcceptComplete
import com.example.lastmailattendent.responses.TicketDetails
import com.example.lastmailattendent.responses.login.LoginResponse
import com.example.lastmailattendent.responses.ticketlist.TicketList
import com.example.lastmailattendent.responses.updateprofile.ProfileUpdate
import com.example.lastmailattendent.responses.user.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap


interface MyApi {

    @Headers("Content-Type:application/json", "organization-key:80b2f8f25c554f9705bb216c8128ba4f05bb0cfd", "location:aurika-coorg")
    @POST("user/auth-login/")
    suspend fun userLogin(@Body map: HashMap<String?, String?>): Response<LoginResponse>

    @Headers("Content-Type:application/json", "organization-key:80b2f8f25c554f9705bb216c8128ba4f05bb0cfd", "location:aurika-coorg")
    @GET("user/list-ticket/")
    suspend fun getTicketList(@HeaderMap headers:HashMap<String?,String?>): Response<TicketList>

    @Headers("Content-Type:application/json", "organization-key:80b2f8f25c554f9705bb216c8128ba4f05bb0cfd", "location:aurika-coorg")
    @GET("user/list-ticket/{id}/")
    suspend fun getTicketDetails(@HeaderMap headers:HashMap<String?,String?>,@Path("id") int:Int ): Response<TicketDetails>

    @Headers("Content-Type:application/json", "organization-key:80b2f8f25c554f9705bb216c8128ba4f05bb0cfd", "location:aurika-coorg")
    @POST
    suspend fun postAcceptTkt(@HeaderMap headerMap: HashMap<String?, String?>,@Body mAcceptCompleteModel: HashMap<String?, String?>,@Url url:String): Response<AcceptComplete>

    @Headers("Content-Type:application/json", "organization-key:80b2f8f25c554f9705bb216c8128ba4f05bb0cfd", "location:aurika-coorg")
    @GET("user/{id}/")
    suspend fun getProfileDetails(@HeaderMap headers:HashMap<String?,String?>,@Path("id") int:Int ): Response<User>


    @Headers("Content-Type:application/json", "organization-key:80b2f8f25c554f9705bb216c8128ba4f05bb0cfd", "location:aurika-coorg")
    @PUT("user/{id}/")
    suspend fun postProfileDetails(@HeaderMap headers:HashMap<String?,String?>,@Path("id") int:Int,@Body profileDetails:HashMap<String?,Any?> ): Response<ProfileUpdate>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
/*
                .baseUrl("https://dev.mobisprint.com:8006/api/v1/aurika/")
*/
                .baseUrl("https://exoneapi.aurikahotels.com:9001/api/v1/aurika/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}
