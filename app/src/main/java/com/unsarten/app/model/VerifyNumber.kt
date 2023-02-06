package com.unsarten.app.model

import java.io.Serializable

data class VerifyNumber(
    val data: VerifyNumberData
): Serializable

data class VerifyNumberData (
    val VerifyNumber: VerifyNumberClass,
    val error: Any? = null
): Serializable

data class VerifyNumberClass (
    val phoneNumber: String,
    val isVerified: Boolean,
    val userId: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
): Serializable
