package com.quranali.pos.screens.home

import com.quranali.pos.data.local.entity.ProductEntity
import com.quranali.pos.domain.model.Category

data class HomeUiState(
    val isLoading: Boolean = true,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
    val productsList: MutableList<ProductEntity> = mutableListOf(),
    val categoriesList: MutableList<Category> = mutableListOf(),
    val selectedCategoryIndex: Int = 0,
    val selectedProductList: MutableList<SelectedProduct> = mutableListOf(),
    val total: String? = null,
    val discount: String? = null,
    val userName: String = "Ali Alquran",
    val userID: String = "89891899",
)
