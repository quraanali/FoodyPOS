package com.quranali.pos.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranali.pos.data.local.entity.ProductEntity
import com.quranali.pos.domain.usecase.ObserveAllProductsUseCase
import com.quranali.pos.domain.usecase.ObserveCategoriesUseCase
import com.quranali.pos.data.remote.network.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val application: Application,
//    private val createOrderUseCase: CreateOrderUseCase,
//    private val calculateTotalAndDiscountUseCase: CalculateTotalAndDiscountUseCase,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,


    ) : ViewModel() {
    private val networkMonitor = NetworkMonitor(application)
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                isLoading = true,
            )
        }

        // observing intenet connectivity
        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                // for UI
                _uiState.update { current ->
                    current.copy(isConnected = isConnected)
                }
            }
        }


        viewModelScope.launch {
            observeCategoriesUseCase().collectLatest { list ->
                _uiState.update {
                    it.copy(categoriesList = list.toMutableList(), isLoading = false)
                }
            }
        }


        viewModelScope.launch {
            observeAllProductsUseCase().collectLatest { list ->
                val productList = mutableListOf<ProductEntity>()
                list.forEach { item ->
                    productList.add(
                        ProductEntity(
                            id = item.id,
                            name = item.name,
                            description = item.description,
                            image = item.image,
                            price = item.price,
                            categoryId = item.categoryId,
                            isAvailable = item.price > 1.0
                        )
                    )

                }
                _uiState.update {
                    it.copy(productsList = productList, isLoading = false)
                }
            }
        }
    }

    fun addProductToCart(product: ProductEntity) {
        if (product.isAvailable) {
            _uiState.update {
                it.copy(
                    errorMessage = "OPS! Out of stock"
                )
            }
            return@addProductToCart
        }

//            _uiState.update { currentState ->
//                val existing = currentState.selectedProductList.find { it.id == product.id }
//
//                val updatedList = if (existing != null) {
//                    currentState.selectedProductList.map {
//                        if (it.id == product.id) it.copy(quantity = it.quantity + 1) else it
//                    }
//                } else {
//                    currentState.selectedProductList + SelectedProduct(
//                        id = product.id,
//                        name = product.name,
//                        price = product.price,
//                        quantity = 1,
//                        taxable = product.taxable,
//                        thumb = product.thumb
//                    )
//                }
//
//                currentState.copy(selectedProductList = updatedList.toMutableList())
//            }
//            calculateTotal()
    }

    fun removeProductFromCart(product: SelectedProduct) {
//            _uiState.update { currentState ->
//                val existing = currentState.selectedProductList.find { it.id == product.id }
//
//                val updatedList = when {
//                    existing == null -> currentState.selectedProductList
//                    existing.quantity > 1 -> currentState.selectedProductList.map {
//                        if (it.id == product.id) it.copy(quantity = it.quantity - 1) else it
//                    }
//
//                    else -> currentState.selectedProductList.filterNot { it.id == product.id }
//                }
//
//                currentState.copy(selectedProductList = updatedList.toMutableList())
//            }
//            calculateTotal()
    }

    fun calculateTotal() {
        val state = _uiState.value

//        val pair = calculateTotalAndDiscountUseCase(state.selectedProductList)

//        _uiState.update {
//            it.copy(
//                total = pair.first,
//                discount = pair.second
//            )
//        }
    }

    fun clearErrorMsg() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }


    override fun onCleared() {
        super.onCleared()
        networkMonitor.stop()
    }

    fun checkoutOrder() {
        _uiState.update {
            it.copy(
                isLoading = true
            )
        }

//        viewModelScope.launch {
//            if (createOrderUseCase(
//                    application = application,
//                    _uiState.value.selectedProductList.toString()
//                )
//            ) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        selectedProductList = mutableListOf(),
//                        total = null,
//                        errorMessage = "Checkout Success!",
//                        discount = null
//                    )
//                }
//            } else {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        selectedProductList = mutableListOf(),
//                        total = null,
//                        discount = null,
//                        errorMessage = "Checkout went wrong!, Switch to OFFLINE mode"
//                    )
//
//                }
//            }
//        }
    }

    fun selectCategory(index: Int) {
        _uiState.update {
            it.copy(selectedCategoryIndex = index)
        }
    }

    fun searchProducts(searchText: String) {

    }
}
