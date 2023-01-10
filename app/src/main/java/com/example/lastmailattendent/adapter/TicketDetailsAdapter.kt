package com.example.lastmailattendent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lastmailattendent.R
import com.example.lastmailattendent.responses.ticketlist.Details

import kotlinx.android.synthetic.main.ticket_details_exchange_content.view.*
import kotlinx.android.synthetic.main.ticket_details_travel_content.view.*
import kotlinx.android.synthetic.main.ticket_details_travel_content.view.tv_price
import kotlinx.android.synthetic.main.ticket_details_travel_content.view.tv_quantity
import kotlinx.android.synthetic.main.ticket_details_travel_content.view.tv_service

class TicketDetailsAdapter(
    private val details: List<Details>
) : RecyclerView.Adapter<TicketDetailsAdapter.BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.ticket_details_recycler_content, parent, false)
        return ViewHolder(view);

    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

           return (holder as ViewHolder).bind(position);

    }




    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(item: Int) {

            itemView.tv_service.setText(details[item].title)

            // holder.tv_service.setText(details.get(position).getTitle());
            // holder.tv_service.setText(details.get(position).getTitle());
            if (!details[item].quantity.isEmpty()) {
                itemView.tv_quantity.setText("x" + details[item].quantity)
            } else {
                itemView.tv_quantity.setText("-")
            }
            if (!details[item].price.isEmpty()) {
                itemView.tv_price.setText(details[item].price + details[item].curency_symbol)
            } else {
                itemView.tv_price.setText("-")
            }
        }


    }



    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
        abstract fun bind(item: Int)
    }

}






