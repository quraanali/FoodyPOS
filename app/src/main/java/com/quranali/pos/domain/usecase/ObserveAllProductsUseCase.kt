package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.repository.MainRepository
import com.quranali.pos.domain.model.Product
import kotlinx.coroutines.flow.Flow

class ObserveAllProductsUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke(): Flow<List<Product>> {
        return repository.observeProducts()
    }
}