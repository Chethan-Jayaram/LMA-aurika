package com.example.lastmailattendent.responses.user

data class Groups(
    val id: Int,
    val name: String,
    val permissions: List<Int>
)