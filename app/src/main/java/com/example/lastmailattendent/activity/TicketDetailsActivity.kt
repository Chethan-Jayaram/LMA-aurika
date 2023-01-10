package com.example.lastmailattendent.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lastmailattendent.R
import com.example.lastmailattendent.activityviewmodel.TicketDetailsViewModel
import com.example.lastmailattendent.adapter.TicketDetailsAdapter
import com.example.lastmailattendent.adapter.TicketDetailsStatusAdapter
import com.example.lastmailattendent.databinding.TicketDetailsActivityBinding
import com.example.lastmailattendent.helper.GlobalClass
import com.example.lastmailattendent.helper.ScoketNotificationListner
import com.example.lastmailattendent.helper.TicketDetailsListner
import com.example.lastmailattendent.responses.TicketDetails
import com.example.lastmailattendent.responses.ticketlist.Data
import com.example.lastmailattendent.responses.ticketlist.Details
import com.example.lastmailattendent.responses.ticketlist.TicketActivity
import com.example.lastmailattendent.util.changeColor
import com.example.lastmailattendent.util.hide
import com.example.lastmailattendent.util.show
import com.example.lastmailattendent.viewmodelfactoy.TicketDetailsViewModelFactory
import kotlinx.android.synthetic.main.activivty_home.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.ticket_details_activity.*
import kotlinx.android.synthetic.main.ticket_details_activity.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class TicketDetailsActivity : AppCompatActivity(), TicketDetailsListner, KodeinAware,
    ScoketNotificationListner {
    private var id: String? = null
    override val kodein: Kodein by kodein()

    private val factory: TicketDetailsViewModelFactory by instance()

    lateinit var DetailsModel: TicketDetailsViewModel

    lateinit var binding: TicketDetailsActivityBinding

    var totaltime: Long? = null
    var timer: CountDownTimer? = null
    lateinit var mReasonSialoug: AlertDialog
    var messageStr = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = DataBindingUtil.setContentView(this, R.layout.ticket_details_activity)
            toolbar_title.setText("Ticket Details")

            /*     iv_profile.setImageDrawable(
                 ContextCompat.getDrawable(
                     applicationContext,
                     R.drawable.ic_back
                 )
             )*/

            iv_more.visibility = View.GONE

            DetailsModel = ViewModelProvider(this, factory).get(TicketDetailsViewModel::class.java)

            DetailsModel.TicketDetailListner = this

            id = intent.getStringExtra("id")
            DetailsModel.getTicketDetails(id!!.toInt())


/*

        iv_profile.setOnClickListener {
            onBackPressed()
        }
*/
            nav_menu.setOnClickListener { v: View? ->
                onBackPressed()
            }

            nav_menu.setImageResource(R.drawable.ic_back);
        }catch(e :Exception){
            e.message
        }

    }


    override fun onStarted() {
        binding.lytTktDtls.visibility = View.GONE
        progress_bar.show()
    }


    override fun onSuccess(ticketList: TicketDetails) {
        DetailsModel.ticketDetails?.let {
            it.observe(this, Observer {
                binding.result = it
                binding.viewModel = DetailsModel
                binding.tvTicketStatus.changeColor(it.current_status.event_style.ticketStatusPills.background)
                if (!it.special_instructions.isEmpty()) {
                    special_instruction_lyt.visibility = View.VISIBLE
                    tv_special_instruction.setText(it.special_instructions)
                } else {
                    special_instruction_lyt.visibility = View.GONE
                }
                totaltime = TimeUnit.MINUTES.toMillis(it.current_level.sla_time_in_minutes.toLong())
                val timeRemaining = GlobalClass.calculateTimeRemaining(
                    it.last_escalated_date_time,
                    it.current_level.sla_time_in_minutes,
                    it
                )
                if (timeRemaining > 0 && !(it.current_status.event_name.equals("complete"))) {
                    timer?.cancel()
                    startTimer(timeRemaining, totaltime!!)
                }
                if (it.current_status.event_name.equals("complete", ignoreCase = true)) {
                    binding.btnComplete.visibility = View.GONE
                }
                //layoutDecider(binding)
                setTicketStatusList(it.ticketActivity)
                setTicketDetails(it.details)
                progress_bar.hide()
                binding.lytTktDtls.visibility = View.VISIBLE
            })
        }
    }


    private fun setTicketDetails(details: List<Details>) {
        try {
            val mAdapter = TicketDetailsAdapter(details)

            tikcet_details_recylcer.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mAdapter
            }
        }catch(e:Exception){
            e.message
        }
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        GlobalClass.ShowAlertDailog(this, "Alert", message)
    }

    override fun onEscaltedReason(str: Int) {
        reasoningDailoug(str)
    }

    fun setTicketStatusList(ticketStatus: List<TicketActivity>) {

        val mAdapter = TicketDetailsStatusAdapter(ticketStatus)

        ticket_status_adapter.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

   /* private fun layoutDecider(itemView: TicketDetailsActivityBinding) {
        when (lyt) {
            TicketDetailsAdapter.PRICEONLY -> itemView.tvQuantity.setVisibility(View.GONE)
            TicketDetailsAdapter.QUANTITYONLY -> itemView.tvPrice.setVisibility(View.GONE)
            TicketDetailsAdapter.ITEMONLY -> {
                itemView.tvPrice.setVisibility(View.GONE)
                itemView.tvQuantity.setVisibility(View.GONE)
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        try {
            HomeActivity.notificationListner = this
            DetailsModel.connectWebSocket()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            timer?.cancel()
            DetailsModel.mWebSocketClient.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun reasoningDailoug(int: Int) {
        try {
            val et_reason: EditText
            val btn_cancel: TextView
            val btn_submit: TextView
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            val viewGroup: ViewGroup = this.findViewById(R.id.main_lyt)
            //then we will inflate the custom alert dialog xml that we created
            val dialogView: View = LayoutInflater.from(this)
                .inflate(R.layout.ticket_escaltion_alert_view, viewGroup, false)
            //Now we need an AlertDialog.Builder object
            val builder = AlertDialog.Builder(this)
            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView)
            //finally creating the alert dialog and displaying it
            mReasonSialoug = builder.create()
            btn_cancel = dialogView.findViewById(R.id.btn_cancel)
            et_reason = dialogView.findViewById(R.id.et_reason)
            btn_submit = dialogView.findViewById(R.id.btn_submit)
            mReasonSialoug.show()
            mReasonSialoug.setCancelable(false)
            btn_cancel.setOnClickListener { mReasonSialoug.dismiss() }
            btn_submit.setOnClickListener {
                try {
                    if (!et_reason.text.toString().isEmpty()) {
                        messageStr = et_reason.text.toString()
                        DetailsModel.getMessage(messageStr, int)
                        mReasonSialoug.dismiss()
                    } else {
                        GlobalClass.ShowAlertDailog(this, "Alert", "Field cant be left blank")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    private fun startTimer(timeRemaining: Long, totaltime: Long) {
        try {
            timer = object : CountDownTimer(timeRemaining, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {

                    try {
                        binding.tvTimer.setText(
                            "" + TimeUnit.MILLISECONDS.toHours(millisUntilFinished) + ":"
                                    + (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60) +
                                    ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)
                        )
                        binding.timerProgressbar.setProgress(100 - (totaltime!! - millisUntilFinished).toInt() / (totaltime!! / 100).toInt())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFinish() {
                    binding.tvTimer.setText("00:00:00")
                    binding.timerProgressbar.setProgress(100)
                }
            }
            timer!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun OnNotificationListner(res: Data) {
        GlobalClass.notifcation(getWindow().getDecorView().getRootView() as ViewGroup, res!!, this)
    }


}