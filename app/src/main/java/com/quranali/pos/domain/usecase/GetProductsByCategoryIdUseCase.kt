package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.model.Product
import com.quranali.pos.domain.repository.MainRepository

class GetProductsByCategoryIdUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke(categoryId: String): List<Product> {
        return repository.getProductsByCategory(categoryId)
    }
}