package com.example.lastmailattendent.responses

import com.example.lastmailattendent.responses.ticketlist.Data


data class TicketDetails(
    val status : Boolean,
    val message : String,
    val data : Data
)