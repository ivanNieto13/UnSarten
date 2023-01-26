package com.unsarten.app.service.lib

import com.unsarten.app.dto.SaveUserDataInput
import com.unsarten.app.model.VerifyNumber
import com.unsarten.app.dto.VerifyCodeInput
import com.unsarten.app.dto.VerifyNumberInput
import com.unsarten.app.model.SaveUserData
import com.unsarten.app.model.VerifyCode
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("login/verifyNumber")
    suspend fun verifyNumber(@Body input: VerifyNumberInput): Response<VerifyNumber>

    @POST("login/verifyCode")
    suspend fun verifyCode(@Body input: VerifyCodeInput): Response<VerifyCode>

    @POST("login/saveUserData")
    suspend fun saveUserData(@Body input: SaveUserDataInput): Response<SaveUserData>
}