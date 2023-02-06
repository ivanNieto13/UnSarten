package com.unsarten.app.model

data class GetOrders (
    val data: GOData
)

data class GOData (
    val GetOrders: List<GetOrder>,
    val error: Any? = null
)

data class GetOrder (
    val userId: String,
    val orderName: String,
    val budget: Double,
    val persons: Long,
    val orderPicture: String? = null,
    val optionalIngredients: String? = null,
    val orderStatus: String
)
