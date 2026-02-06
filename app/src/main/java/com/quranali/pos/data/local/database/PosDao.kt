package com.quranali.pos.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quranali.pos.data.local.entity.CategoryEntity
import com.quranali.pos.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PosDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAllCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    fun observeProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    suspend fun getProductsByCategory(categoryId: String): List<ProductEntity>

//    @Query("  SELECT * FROM products  WHERE name LIKE '%' || :productName || '%'")
//    suspend fun searchProductsByName(productName: String): Flow<List<ProductEntity>>

}