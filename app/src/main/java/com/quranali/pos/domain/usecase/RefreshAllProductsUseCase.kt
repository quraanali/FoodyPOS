package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.repository.MainRepository

class RefreshAllProductsUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke() {
        repository.syncProducts()
    }
}