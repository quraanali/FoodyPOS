package com.quranali.pos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val isAvailable: Boolean,
    val categoryId: String
)