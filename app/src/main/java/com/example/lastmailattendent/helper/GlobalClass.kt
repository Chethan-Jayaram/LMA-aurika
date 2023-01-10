

package com.example.lastmailattendent.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Handler
import android.text.format.Time
import android.util.Base64
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.lastmailattendent.R
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.util.hideLinerlyt
import java.io.ByteArrayOutputStream

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@SuppressLint("SimpleDateFormat")
object GlobalClass {
    var count:Int=0

    val inputTimeFormat=SimpleDateFormat("hh:mm:ss")
    val outputTimeFormat=SimpleDateFormat("HH:mm:ss a")

    val pstFormat=SimpleDateFormat("MMM d, yyyy h:mm:ss a")
    val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val escalationFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm")

    val androidFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    val timeFormat = SimpleDateFormat("MMM d, yyyy hh:mm:ss aa")
    var userID:Int=0
    var employeID=""


    var url=""
    var  mUserName=""

    lateinit var alertDialog: AlertDialog


    @JvmStatic
    fun dateTimeConverter(date: String):String{
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            pstFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            return pstFormat.format(utcFormat.parse(date)!!)
    }

     fun ShowAlertDailog(context: Context, alert: String, message: String) {
        alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(alert)
        alertDialog.setMessage(message)
        alertDialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            "ok",
            { dialog, which -> alertDialog.dismiss() })
        alertDialog.show()
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
            .setTextColor(context.resources.getColor(R.color.black))
        alertDialog.setCancelable(false)
    }




    fun Shownotification(context: Context, alert: String, message: String){
        try {
            alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setTitle(alert)
            alertDialog.setMessage(message)
            alertDialog.show()
        }catch (e: java.lang.Exception){
            e.printStackTrace();
        }

    }
     //working code back up timer calculations
/*     fun calculateTimeRemaining(lastEscalatedDateTime: String,slaTimeInMinutes: Int):Long {
         val mydate= dateTimeConverter(lastEscalatedDateTime)
         val last_escalted_time = timeFormat.format(pstFormat.parse(mydate)!!)

         val current_time=timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

         val timeDiffernce=(timeFormat.parse(current_time)!!.time-timeFormat.parse(last_escalted_time)!!.time)

         val yimeinmilliseconds=TimeUnit.MINUTES.toMillis(slaTimeInMinutes.toLong())

         val finalDiifernceInMilliSeconds=yimeinmilliseconds-timeDiffernce

          return finalDiifernceInMilliSeconds
    }*/


    fun calculateTimeRemaining(lastEscalatedDateTime: String, slaTimeInMinutes: Int, data: Data):Long {
       var  finalDiifernceInMilliSeconds:Long=0
       if( data.current_status.event_name.equals(
               "assigned",
               ignoreCase = true
           )){
           if(data.Deliverydate!=null){
               val deliverydate = escalationFormat.parse(data.Deliverydate)
               deliverydate.hours=data.DeliveryTIme.split(":")[0].toInt()
               deliverydate.minutes = data.DeliveryTIme.split(":")[1].toInt()
               deliverydate.seconds = data.DeliveryTIme.split(":")[2].toInt()


               val mydate = escalationFormat.parse(lastEscalatedDateTime)
               val last_escalted_time = timeFormat.format(mydate!!)
               val current_time1 =
                   timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

               val calculateslatime=(deliverydate.time - timeFormat.parse(current_time1)!!.time);


               val current_time =
                   timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

               val timeDiffernce =
                   (timeFormat.parse(current_time)!!.time - timeFormat.parse(last_escalted_time)!!.time)



               finalDiifernceInMilliSeconds = calculateslatime - timeDiffernce

           }else if(data.DeliveryTIme!=null){

               var date = Calendar.getInstance().time
               date.hours =data.DeliveryTIme.split(":")[0].toInt()
               date.minutes = data.DeliveryTIme.split(":")[1].toInt()
               date.seconds = data.DeliveryTIme.split(":")[2].toInt()

               val mydate = escalationFormat.parse(lastEscalatedDateTime)
               val last_escalted_time = timeFormat.format(mydate!!)
               val current_time1 =
                   timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

               val calculateslatime=(date.time - timeFormat.parse(current_time1)!!.time);


               val current_time =
                   timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

               val timeDiffernce =
                   (timeFormat.parse(current_time)!!.time - timeFormat.parse(last_escalted_time)!!.time)



               finalDiifernceInMilliSeconds = calculateslatime - timeDiffernce

           }else{
               val mydate = escalationFormat.parse(lastEscalatedDateTime)
               val last_escalted_time = timeFormat.format(mydate!!)

               val current_time =
                   timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

               val timeDiffernce =
                   (timeFormat.parse(current_time)!!.time - timeFormat.parse(last_escalted_time)!!.time)

               val yimeinmilliseconds = TimeUnit.MINUTES.toMillis(slaTimeInMinutes.toLong())

               finalDiifernceInMilliSeconds = yimeinmilliseconds - timeDiffernce
           }

       }else {
           val mydate = escalationFormat.parse(lastEscalatedDateTime)
           val last_escalted_time = timeFormat.format(mydate!!)

           val current_time =
               timeFormat.format(androidFormat.parse(Calendar.getInstance().time.toString())!!)

           val timeDiffernce =
               (timeFormat.parse(current_time)!!.time - timeFormat.parse(last_escalted_time)!!.time)

           val yimeinmilliseconds = TimeUnit.MINUTES.toMillis(slaTimeInMinutes.toLong())

            finalDiifernceInMilliSeconds = yimeinmilliseconds - timeDiffernce

       }

        return finalDiifernceInMilliSeconds
    }




    @JvmStatic
    fun acceptOrCompleteTkt(result: Data?): String {
        if (result?.assignee != null) {
            return "Complete"
        } else {
            return "Accept"
        }
    }


    fun encodeTobase64(image: Bitmap): String? {
        var imageEncoded: String? = ""
        try {
            val byteArrayOutputStream =
                ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val b = byteArrayOutputStream.toByteArray()
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imageEncoded
    }


     fun notifcation(vg: ViewGroup, res: Data, ctx: Context) {
        try {
            lateinit var mReasonSialoug: AlertDialog
            val tv_ticket_no: TextView
            val tv_assigned_to:TextView
            val tv_ticket_status:TextView
            val lvt_assignee:LinearLayout
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            val viewGroup: ViewGroup = vg.findViewById(R.id.relative_lyt)
            //then we will inflate the custom alert dialog xml that we created
            val dialogView: View = LayoutInflater.from(ctx)
                .inflate(R.layout.in_app_notificaton_content, viewGroup, false)
            //Now we need an AlertDialog.Builder object
            val builder = AlertDialog.Builder(ctx)
            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView)
            //finally creating the alert dialog and displaying it
            mReasonSialoug = builder.create()
            tv_ticket_no = dialogView.findViewById(R.id.tv_ticket_no)
            tv_assigned_to = dialogView.findViewById(R.id.tv_assigned_to)
            tv_ticket_status = dialogView.findViewById(R.id.tv_ticket_status)
            lvt_assignee = dialogView.findViewById(R.id.lvt_assignee)
            if(res.assignee==null) run {
                lvt_assignee.hideLinerlyt()
            }
            tv_ticket_no.setText(res.ticket_number)
            res.assignee.let {
                tv_assigned_to.setText(it?.first_name)
            }

            tv_ticket_status.setText(res.current_status.event_name)
            val window: Window = mReasonSialoug.getWindow()!!
            val wlp: WindowManager.LayoutParams = window.getAttributes()

            wlp.gravity = Gravity.TOP
            wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            window.setAttributes(wlp)
            mReasonSialoug.show()
            mReasonSialoug.setCancelable(true)
            Handler().postDelayed({
                mReasonSialoug.dismiss()
            }, 2000)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }



}