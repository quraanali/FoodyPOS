package com.quranali.pos.screens.home

import kotlinx.serialization.Serializable

@Serializable
data class SelectedProduct(
    var id: Int,
    var name: String,
    var price: Double,
    var quantity: Int,
    var taxable: Boolean,
    var thumb: String
)