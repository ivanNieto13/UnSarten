package com.unsarten.app.model

import java.io.Serializable

data class VerifyCode (
    val data: VerifyCodeData
): Serializable

data class VerifyCodeData (
    val VerifyCode: VerifyCodeClass,
    val error: Any? = null
): Serializable

data class VerifyCodeClass (
    val code: String,
    val isValid: Boolean
): Serializable
