package com.example.lastmailattendent.responses

import com.example.lastmailattendent.responses.ticketlist.Data

data class GeneralPojo(
    val type : String,
    val event : String,
    val message : Data
)