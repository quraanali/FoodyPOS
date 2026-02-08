package com.quranali.pos.domain.repository

import com.quranali.pos.domain.model.Category
import com.quranali.pos.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun syncCategories()
    fun observeCategories(): Flow<List<Category>>

    suspend fun syncProducts()
    fun observeProducts(): Flow<List<Product>>

    suspend fun getSearchProductsByName(productName: String): List<Product>
    suspend fun getProductsByCategory(categoryId: String): List<Product>
}