package com.example.lastmailattendent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lastmailattendent.R
import com.example.lastmailattendent.databinding.TicketStatusRecyclerContentBinding
import com.example.lastmailattendent.responses.ticketlist.TicketActivity

class TicketDetailsStatusAdapter(
    private val ticket: List<TicketActivity>
) : RecyclerView.Adapter<TicketDetailsStatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myview: TicketStatusRecyclerContentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext())
            , R.layout.ticket_status_recycler_content, parent, false
        )
        return ViewHolder(myview)
    }

    override fun getItemCount(): Int {
        return ticket.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tickletBinding.ticket=ticket.get(position)

    }

    class ViewHolder(itemview: TicketStatusRecyclerContentBinding) :
        RecyclerView.ViewHolder(itemview.root) {
        var tickletBinding: TicketStatusRecyclerContentBinding

        init {
            tickletBinding = itemview
        }
    }
}