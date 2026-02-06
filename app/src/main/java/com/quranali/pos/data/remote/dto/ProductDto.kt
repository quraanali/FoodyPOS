package com.quranali.pos.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    var id: String,
    var name: String,
    var description: String,
    var image: String,
    var price: Double,
    var isAvailable: Boolean,
    var categoryId: String,
)