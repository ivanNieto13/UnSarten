package com.unsarten.app.dto

data class VerifyNumberInput(
    val phoneNumber: String
)

data class VerifyCodeInput(
    val code: String
)

data class SaveUserDataInput(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
)
