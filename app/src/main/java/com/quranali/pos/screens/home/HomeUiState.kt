package com.quranali.pos.screens.home

import com.quranali.pos.domain.model.CartItem
import com.quranali.pos.domain.model.Category
import com.quranali.pos.domain.model.Product

data class HomeUiState(
    val isLoading: Boolean = true,
    val isConnected: Boolean = false,
    val errorMessage: String? = null,
    val productsList: List<Product> = emptyList(),
    val categoriesList: List<Category> = emptyList(),
    val selectedCategoryIndex: Int = 0,
    val cartList: List<CartItem> = emptyList(),
    val total: String? = null,
    val userName: String = "Ali Alquran",
    val userID: String = "89891899",
)
