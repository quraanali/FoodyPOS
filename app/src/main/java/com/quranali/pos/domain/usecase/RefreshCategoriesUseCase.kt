package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.repository.MainRepository

class RefreshCategoriesUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke() {
        repository.syncCategories()
    }
}