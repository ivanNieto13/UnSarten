package com.unsarten.app.model

data class Order(var userId: String, var orderName: String, var budget: Double, var persons: Long, var optionalIngredients: String?, var orderPicture: String?, var date: String, var author: String,var orderStatus: String)
