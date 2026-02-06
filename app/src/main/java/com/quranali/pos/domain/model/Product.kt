package com.quranali.pos.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val categoryId: String,
    val isAvailable: Boolean
)