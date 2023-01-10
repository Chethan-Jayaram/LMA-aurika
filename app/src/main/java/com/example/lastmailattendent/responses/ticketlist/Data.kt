/* 
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

package com.example.lastmailattendent.responses.ticketlist
 class Data (

	val id : Int,
	val ticket_number : String,
	val escalated_level : String,
	val title : String,
	val start_date_time : String,
	val end_date_time : String,
	val last_activity_on : String,
	val location_id : Int,
	val last_escalated_date_time : String,
	val booking : Booking,
	val level : Int,
	val department : String,
	val current_status : Current_status,
	val assignee : Assignee,
	val current_escalated_level : Current_escalated_level,
	val ticketActivity : List<TicketActivity>,
	val current_level : Current_level,
	val layout : String,
	val special_instructions : String,
	val delivery_type : String,
	val surcharges : String,
	val ticket_items : List<Ticket_items>,
	val hangertype : String,
	val requestDate : String,
	val requestHours : String,
	val requestTime : String,
	val Deliverydate : String,
	val DeliveryTIme : String,
	val room_number : String,
	val details : List<Details>
)