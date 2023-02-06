package com.unsarten.app.service.lib

import com.unsarten.app.dto.SaveOrderDataInput
import com.unsarten.app.model.GetOrders
import com.unsarten.app.model.SaveOrderData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrdersAPI {
    @POST("orders/saveOrderData")
    suspend fun saveOrderData(@Body input: SaveOrderDataInput): Response<SaveOrderData>

    @GET("orders/getOrders")
    suspend fun getOrders(): Response<GetOrders>
}