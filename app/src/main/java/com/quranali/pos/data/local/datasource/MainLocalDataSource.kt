package com.quranali.pos.data.local.datasource

import com.quranali.pos.data.local.entity.CategoryEntity
import com.quranali.pos.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface MainLocalDataSource {

    suspend fun saveCategories(categories: List<CategoryEntity>)
    fun observeCategories(): Flow<List<CategoryEntity>>

    suspend fun saveProducts(products: List<ProductEntity>)
    fun observeProducts(): Flow<List<ProductEntity>>
}