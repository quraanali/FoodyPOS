package com.quranali.pos.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranali.pos.R
import com.quranali.pos.data.remote.network.NetworkMonitor
import com.quranali.pos.domain.model.CartItem
import com.quranali.pos.domain.model.Category
import com.quranali.pos.domain.model.Product
import com.quranali.pos.domain.usecase.CalculateTotalPriceUseCase
import com.quranali.pos.domain.usecase.GetProductsByCategoryIdUseCase
import com.quranali.pos.domain.usecase.GetSearchProductsByNameUseCase
import com.quranali.pos.domain.usecase.ObserveAllProductsUseCase
import com.quranali.pos.domain.usecase.ObserveCategoriesUseCase
import com.quranali.pos.domain.usecase.RefreshAllProductsUseCase
import com.quranali.pos.domain.usecase.RefreshCategoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val application: Application,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
    private val refreshCategoriesUseCase: RefreshCategoriesUseCase,
    private val refreshAllProductsUseCase: RefreshAllProductsUseCase,
    private val getSearchProductsByNameUseCase: GetSearchProductsByNameUseCase,
    private val getProductsByCategoryIdUseCase: GetProductsByCategoryIdUseCase,
    private val calculateTotalPriceUseCase: CalculateTotalPriceUseCase
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

        viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                // to load data when connected to the internet
                if (isConnected && (_uiState.value.productsList.isEmpty() || _uiState.value.categoriesList.isEmpty())) {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                        )
                    }
                    refreshCategoriesUseCase()
                    refreshAllProductsUseCase()
                }

                // for UI
                _uiState.update { current ->
                    current.copy(isConnected = isConnected)
                }
            }
        }


        viewModelScope.launch {
            observeCategoriesUseCase().collectLatest { list ->
                val categoryList = mutableListOf<Category>()
                categoryList.add(Category("-1", "All"))
                categoryList.addAll(list)

                _uiState.update {
                    it.copy(categoriesList = categoryList, isLoading = false)
                }
            }
        }

        setAllProductsToUi()
    }

    fun addProductToCart(productUi: ProductUi) {
        if (!productUi.product.isAvailable) {
            _uiState.update { it.copy(errorMessage = application.getString(R.string.outOfStock)) }
            return
        }

        _uiState.update { currentState ->
            val existing = currentState.cartList.find { it.product.id == productUi.product.id }
            val updatedCart = if (existing != null) {
                currentState.cartList.map {
                    if (it.product.id == productUi.product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentState.cartList + CartItem(product = productUi.product, quantity = 1)
            }

            val updatedProducts = currentState.productsList.map { productUi ->
                val cartItem = updatedCart.find { it.product.id == productUi.product.id }
                productUi.copy(quantity = cartItem?.quantity)
            }

            currentState.copy(
                cartList = updatedCart,
                productsList = updatedProducts
            )
        }
        calculateTotal()
    }


    private fun setAllProductsToUi() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            observeAllProductsUseCase().collect { list ->
                val currentCart = _uiState.value.cartList

                val productUiList = list.map { item ->
                    val product = Product(
                        id = item.id,
                        name = item.name,
                        description = item.description,
                        image = item.image,
                        price = item.price,
                        categoryId = item.categoryId,
                        isAvailable = item.price > 1.0
                    )
                    val quantityInCart = currentCart.find { it.product.id == item.id }?.quantity
                    ProductUi(product = product, quantity = quantityInCart)
                }

                _uiState.update {
                    it.copy(productsList = productUiList, isLoading = false)
                }
            }
        }
    }


    fun calculateTotal() {
        _uiState.update {
            it.copy(
                total = calculateTotalPriceUseCase(it.cartList).toString()
            )
        }

    }

    fun clearErrorMsg() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    fun checkoutOrder() {
        _uiState.update { currentState ->
            val clearedProducts = currentState.productsList.map { it.copy(quantity = null) }
            currentState.copy(
                isLoading = false,
                cartList = emptyList(),
                productsList = clearedProducts,
                total = null,
                errorMessage = application.getString(R.string.orderPlaced)
            )
        }
    }

    var filterJob: Job? = null
    fun selectCategory(index: Int) {
        _uiState.update { it.copy(selectedCategoryIndex = index) }

        if (index == 0) {
            setAllProductsToUi()
        } else {
            _uiState.update { it.copy(isLoading = true) }
            filterJob?.cancel()

            val categoryId = _uiState.value.categoriesList[index].id

            filterJob = viewModelScope.launch {
                val searchList = getProductsByCategoryIdUseCase(categoryId)

                _uiState.update { currentState ->
                    currentState.copy(
                        productsList = searchList.mapToProductUi(currentState.cartList),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun List<Product>.mapToProductUi(cartList: List<CartItem>): List<ProductUi> {
        return this.map { product ->
            val quantityInCart = cartList.find { it.product.id == product.id }?.quantity
            ProductUi(
                product = product,
                quantity = quantityInCart
            )
        }
    }

    fun searchProducts(searchText: String) {
        filterJob?.cancel()

        if (searchText.isEmpty()) {
            setAllProductsToUi()
        } else {
            _uiState.update { it.copy(isLoading = true, selectedCategoryIndex = 0) }

            filterJob = viewModelScope.launch {
                val searchList = getSearchProductsByNameUseCase(searchText)

                _uiState.update { currentState ->
                    currentState.copy(
                        productsList = searchList.mapToProductUi(currentState.cartList),
                        isLoading = false,
                        selectedCategoryIndex = 0,
                    )
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        networkMonitor.stop()
    }
}
