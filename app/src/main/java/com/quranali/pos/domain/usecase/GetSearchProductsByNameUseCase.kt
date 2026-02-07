package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.repository.MainRepository
import com.quranali.pos.domain.model.Product
import kotlinx.coroutines.flow.Flow

class GetSearchProductsByNameUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke(productName: String): List<Product> {
        return repository.getSearchProductsByName(productName)
    }
}