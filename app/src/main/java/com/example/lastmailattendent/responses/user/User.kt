package com.example.lastmailattendent.responses.user

data class User(
    val error: String,
    val result: Result,
    val status: String,
    val statusCode: Int
)