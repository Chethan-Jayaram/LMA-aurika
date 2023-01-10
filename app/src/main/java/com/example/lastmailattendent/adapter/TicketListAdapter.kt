package com.example.lastmailattendent.adapter


import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lastmailattendent.R
import com.example.lastmailattendent.activityviewmodel.TicketsListViewModel
import com.example.lastmailattendent.databinding.TicketItemListBinding
import com.example.lastmailattendent.helper.GlobalClass
import com.example.lastmailattendent.helper.OnclickListner
import com.example.lastmailattendent.responses.ticketlist.Data
import java.util.concurrent.TimeUnit


class TicketListAdapter(
    private val result: List<Data>,
    private val listModelList: TicketsListViewModel,
    private val onclick: OnclickListner
) : RecyclerView.Adapter<TicketListAdapter.MyViewHolder>() {


    val map: HashMap<String, Int> = HashMap()
    var totaltime: Long? = null
    val onItemClick: OnclickListner = onclick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myview: TicketItemListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.ticket_item_list, parent, false
        )
        return MyViewHolder(myview)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.ticketItemListBinding.result = result.get(position)
            holder.ticketItemListBinding.viewModel = listModelList
            holder.ticketItemListBinding.executePendingBindings()
            /*     if (result.get(position).current_status.name.contains("2")) {
                     holder.timer?.cancel()
                     holder.ticketItemListBinding.tvTimer.setText("00:00:00")
                     holder.ticketItemListBinding.timerProgressbar.setProgress(100)
                 }else{*/


            handelUiChanges(holder,result,position);

            /*}*/

            /* else if (result.get(position).assignee != null && result.get(position).current_status.event_name.equals(
                    "escalated",
                    ignoreCase = true
                )
            ) {
                holder.timer?.cancel()
                val timeRemaining = GlobalClass.calculateTimeRemaining(
                    result.get(position).last_escalated_date_time,
                    result.get(position).current_level.sla_time_in_minutes
                )
                startTimer(holder, timeRemaining, position)
            } else if (result.get(position).assignee != null && result.get(position).current_status.event_name.equals(
                    "assigned",
                    ignoreCase = true
                )
            ) {
                holder.timer?.cancel()
                val timeRemaining = GlobalClass.calculateTimeRemaining(
                    result.get(position).last_escalated_date_time,
                    result.get(position).current_level.sla_time_in_minutes
                )
                if (timeRemaining > 0) {
                    startTimer(holder, timeRemaining, position)
                } else {
                    holder.ticketItemListBinding.tvTimer.setText("00:00:00")
                    holder.ticketItemListBinding.timerProgressbar.setProgress(100)
                }

            } else if (result.get(position).assignee == null && result.get(position).current_status.event_name.equals(
                    "escalated",
                    ignoreCase = true
                )
            ) {
                holder.timer?.cancel()
                val timeRemaining = GlobalClass.calculateTimeRemaining(
                    result.get(position).last_escalated_date_time,
                    result.get(position).current_level.sla_time_in_minutes
                )
                startTimer(holder, timeRemaining, position)
            } else if (result.get(position).assignee == null && result.get(position).current_status.event_name.equals(
                    "new_ticket",
                    ignoreCase = true
                )
            ) {
                holder.timer?.cancel()
                val timeRemaining = GlobalClass.calculateTimeRemaining(
                    result.get(position).last_escalated_date_time,
                    result.get(position).current_level.sla_time_in_minutes
                )
                startTimer(holder, timeRemaining, position)
            } else {
                holder.timer?.cancel()
                holder.ticketItemListBinding.tvTimer.setText("00:00:00")
                holder.ticketItemListBinding.timerProgressbar.setProgress(100)
            }
*/
            holder.ticketItemListBinding.contentlyt.setOnClickListener {
                try {
                    it.isClickable = false
                    onItemClick.onItemClicked(position)
                }catch (E:Exception){
                    E.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun handelUiChanges(
        holder: MyViewHolder,
        result: List<Data>,
        position: Int
    ) {

        holder.timer?.cancel()
        val timeRemaining = GlobalClass.calculateTimeRemaining(
            result.get(position).last_escalated_date_time,
            result.get(position).current_level.sla_time_in_minutes,result.get(position)
        )
        if (timeRemaining > 0) {
            totaltime =TimeUnit.MINUTES.toMillis(result.get(position).current_level.sla_time_in_minutes.toLong())
            startTimer(holder, timeRemaining, position, totaltime!!)
        } else {
            holder.ticketItemListBinding.tvTimer.setText("00:00:00")
            holder.ticketItemListBinding.timerProgressbar.setProgress(100)
        }

    }


    class MyViewHolder(itemView: TicketItemListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val ticketItemListBinding: TicketItemListBinding
        var timer: CountDownTimer? = null

        init {
            ticketItemListBinding = itemView
        }
        /* override fun preAnimateRemoveImpl(holder: RecyclerView.ViewHolder) {
             // do something
         }

         override fun animateRemoveImpl(holder: RecyclerView.ViewHolder, listener: ViewPropertyAnimatorListener) {
             ViewCompat.animate(itemView).apply {
                 translationY(-itemView.height * 0.3f)
                 alpha(0f)
                 duration = 300
                 setListener(listener)
             }.start()
         }

         override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
             ViewCompat.setTranslationY(itemView, -itemView.height * 0.3f)
             ViewCompat.setAlpha(itemView, 0f)
         }

         override fun animateAddImpl(holder: RecyclerView.ViewHolder, listener: ViewPropertyAnimatorListener) {
             ViewCompat.animate(itemView).apply {
                 translationY(0f)
                 alpha(1f)
                 duration = 300
                 setListener(listener)
             }.start()
         }
 */
    }


/*    private fun resumeTimer(holder: MyViewHolder, time: Long, position: Int) {
        holder.timer= object : CountDownTimer(time,1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                holder.ticketItemListBinding.tvTimer.setText(
                    "" + TimeUnit.MILLISECONDS.toHours(millisUntilFinished) + ":"
                            + (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60) +
                            ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)
                )
                result.get(position).timer_time=
                    ("" + TimeUnit.MILLISECONDS.toHours(millisUntilFinished) + ":"
                            + (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60) +
                            ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60))
                //result.get(position).timer_progress=(totaltime - millisUntilFinished).toInt() / 1200
                changeProgress(holder.ticketItemListBinding.timerProgressbar,millisUntilFinished)
            }
            override fun onFinish() {
                result.get(position).istriggered=false
                holder.ticketItemListBinding.timerProgressbar.setProgress(100)
            }
        }
        holder.timer?.start()
    }*/


    private fun startTimer(holder: MyViewHolder, initialtime: Long, position: Int, totaltime: Long) {
        try {
            holder.timer = object : CountDownTimer(initialtime, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    try {
                        holder.ticketItemListBinding.tvTimer.setText(
                            "" + TimeUnit.MILLISECONDS.toHours(millisUntilFinished) + ":"
                                    + (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60) +
                                    ":" + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)
                        )
                        holder.ticketItemListBinding.timerProgressbar.setProgress(100 - (totaltime!! - millisUntilFinished).toInt() / (totaltime!! / 100).toInt())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFinish() {
                    holder.ticketItemListBinding.tvTimer.setText("00:00:00")
                    holder.ticketItemListBinding.timerProgressbar.setProgress(100)
                }
            }
            holder.timer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


