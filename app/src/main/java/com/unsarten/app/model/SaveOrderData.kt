package com.unsarten.app.model

data class SaveOrderData (
    val data: SOData
)

data class SOData (
    val SaveOrderData: SaveOrderDataClass,
    val error: Any? = null
)

data class SaveOrderDataClass (
    val userId: String,
    val orderName: String,
    val budget: Float,
    val persons: Long,
    val orderPicture: String?,
    val orderStatus: String,
    val optionalIngredients: String,
    val author: String,
)
