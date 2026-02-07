package com.quranali.pos.screens.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranali.pos.data.remote.network.NetworkMonitor
import com.quranali.pos.domain.model.Category
import com.quranali.pos.domain.model.Product
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
//    private val createOrderUseCase: CreateOrderUseCase,
//    private val calculateTotalAndDiscountUseCase: CalculateTotalAndDiscountUseCase,
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
    private val refreshCategoriesUseCase: RefreshCategoriesUseCase,
    private val refreshAllProductsUseCase: RefreshAllProductsUseCase,
    private val getSearchProductsByNameUseCase: GetSearchProductsByNameUseCase,
    private val getProductsByCategoryIdUseCase: GetProductsByCategoryIdUseCase,
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
        /*  viewModelScope.launch {
              observeAllProductsUseCase().collectLatest { list ->
                  val productList = mutableListOf<Product>()
                  list.forEach { item ->
                      productList.add(
                          Product(
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
          }*/
    }

    fun addProductToCart(product: Product) {
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


    private fun setAllProductsToUi() {
        _uiState.update {
            it.copy(productsList = mutableListOf(), isLoading = true)
        }


        viewModelScope.launch {
            observeAllProductsUseCase().collect { list ->
                val productList = mutableListOf<Product>()
                list.forEach { item ->
                    productList.add(
                        Product(
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

    fun removeProductFromCart(product: SelectedProduct) {
    }

    fun calculateTotal() {
        val state = _uiState.value

//        val pair = calculateTotalAndDiscountUseCase(state.selectedProductList)
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
    }


    var categoryJob: Job? = null
    fun selectCategory(index: Int) {
        _uiState.update {
            it.copy(selectedCategoryIndex = index, )
        }
        if (index == 0) {
            setAllProductsToUi()
        } else {
            _uiState.update {
                it.copy(selectedProductList = mutableListOf(), isLoading = true)
            }
            categoryJob?.cancel()
            val categoryId = _uiState.value.categoriesList[index].id

            categoryJob = viewModelScope.launch {
                val searchList = getProductsByCategoryIdUseCase(categoryId)
                _uiState.update {
                    it.copy(productsList = searchList.toMutableList(), isLoading = false)
                }
            }
        }
    }

    var searchJob: Job? = null
    fun searchProducts(searchText: String) {
        searchJob?.cancel()

        if (searchText.isEmpty()) {
            setAllProductsToUi()
        } else {
            _uiState.update {
                it.copy(selectedProductList = mutableListOf(), isLoading = true)
            }
            searchJob = viewModelScope.launch {
                val searchList = getSearchProductsByNameUseCase(searchText)
                _uiState.update {
                    it.copy(productsList = searchList.toMutableList(), isLoading = false)
                }
            }
        }
    }
}
