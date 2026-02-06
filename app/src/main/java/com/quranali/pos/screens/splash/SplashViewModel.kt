package com.quranali.pos.screens.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranali.pos.domain.usecase.RefreshCategoriesUseCase
import com.quranali.pos.domain.usecase.RefreshAllProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SplashViewModel(
    private val refreshCategoriesUseCase: RefreshCategoriesUseCase,
    private val refreshAllProductsUseCase: RefreshAllProductsUseCase,
) : ViewModel() {

    var isLoading by mutableStateOf(true)

    init {
        viewModelScope.launch {
            try {
                val categoriesTask = async(Dispatchers.IO) { refreshCategoriesUseCase() }
                val productsTask = async(Dispatchers.IO) { refreshAllProductsUseCase() }

                categoriesTask.await()
                productsTask.await()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
