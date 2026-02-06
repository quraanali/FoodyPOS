package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.repository.MainRepository
import com.quranali.pos.domain.model.Category
import kotlinx.coroutines.flow.Flow

class ObserveCategoriesUseCase(
    private val repository: MainRepository
) {
    suspend operator fun invoke(): Flow<List<Category>> {
        return repository.observeCategories()
    }
}