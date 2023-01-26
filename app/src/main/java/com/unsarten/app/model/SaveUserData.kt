package com.unsarten.app.model

import java.io.Serializable

data class SaveUserData (
    val data: SUserData
): Serializable

data class SUserData (
    val SaveUserData: SaveUserDataClass,
    val error: Any? = null
): Serializable

data class SaveUserDataClass (
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
): Serializable
