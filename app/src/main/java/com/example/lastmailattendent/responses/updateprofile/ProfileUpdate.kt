package com.example.lastmailattendent.responses.updateprofile

data class ProfileUpdate(
    val error: String,
    val result: Result,
    val status: String,
    val statusCode: Int
)