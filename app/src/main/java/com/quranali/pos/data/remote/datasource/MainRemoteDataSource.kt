package com.quranali.pos.data.remote.datasource

import com.quranali.pos.data.remote.dto.CategoryDto
import com.quranali.pos.data.remote.dto.ProductDto

interface MainRemoteDataSource {
    suspend fun refreshCategories(): List<CategoryDto>
    suspend fun refreshProducts(): List<ProductDto>
}
