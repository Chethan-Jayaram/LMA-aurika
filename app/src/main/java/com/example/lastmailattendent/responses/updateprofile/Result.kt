package com.example.lastmailattendent.responses.updateprofile

data class Result(
    val date_joined: String,
    val email: String,
    val first_name: String,
    val groups: String,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_name: String,
    val password: String,
    val profile: Profile,
    val username: String
)