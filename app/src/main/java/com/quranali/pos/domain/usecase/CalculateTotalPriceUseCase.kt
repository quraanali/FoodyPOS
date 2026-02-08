package com.quranali.pos.domain.usecase

import com.quranali.pos.domain.model.CartItem

class CalculateTotalPriceUseCase() {
    operator fun invoke(cartList: List<CartItem>): Double {
        var price = 0.0
        cartList.forEach { item ->
            price += item.quantity * item.product.price
        }
        return price
    }
}