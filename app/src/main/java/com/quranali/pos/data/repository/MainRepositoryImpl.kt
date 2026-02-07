package com.quranali.pos.data.repository

import com.quranali.pos.data.local.datasource.MainLocalDataSource
import com.quranali.pos.data.mapper.toDomain
import com.quranali.pos.data.mapper.toEntity
import com.quranali.pos.data.remote.datasource.MainRemoteDataSource
import com.quranali.pos.domain.model.Category
import com.quranali.pos.domain.model.Product
import com.quranali.pos.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainRepositoryImpl(
    private val remote: MainRemoteDataSource,
    private val local: MainLocalDataSource,
) : MainRepository {

    override suspend fun syncCategories() {
        val categories = remote.refreshCategories()
            .map { it.toDomain() }
            .map { it.toEntity() }

        local.saveCategories(categories)
    }

    override fun observeCategories(): Flow<List<Category>> {
        return local.observeCategories()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun syncProducts() {
        val products = remote.refreshProducts()
            .map { it.toDomain() }
            .map { it.toEntity() }

        local.saveProducts(products)
    }

    override fun observeProducts(): Flow<List<Product>> {
        return local.observeProducts()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getSearchProductsByName(productName: String): List<Product> {
        return local.getSearchProductsByName(productName).map { it.toDomain() }

    }

    override suspend fun getProductsByCategory(categoryId: String): List<Product> {
        return local.getProductsByCategory(categoryId)
            .map { it.toDomain() }
    }
}
