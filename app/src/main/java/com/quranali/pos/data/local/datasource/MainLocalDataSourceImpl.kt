package com.quranali.pos.data.local.datasource

import com.quranali.pos.data.local.database.PosDao
import com.quranali.pos.data.local.entity.CategoryEntity
import com.quranali.pos.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

class MainLocalDataSourceImpl(
    private val dao: PosDao
) : MainLocalDataSource {

    override suspend fun saveCategories(categories: List<CategoryEntity>) {
        dao.insertAllCategories(categories)
    }

    override fun observeCategories(): Flow<List<CategoryEntity>> {
        return dao.observeCategories()
    }

    override suspend fun saveProducts(products: List<ProductEntity>) {
        dao.insertAllProducts(products)
    }

    override fun observeProducts(): Flow<List<ProductEntity>> {
        return dao.observeProducts()
    }
}
