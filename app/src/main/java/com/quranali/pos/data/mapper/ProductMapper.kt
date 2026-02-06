package com.quranali.pos.data.mapper

import com.quranali.pos.data.local.entity.ProductEntity
import com.quranali.pos.data.remote.dto.ProductDto
import com.quranali.pos.domain.model.Product


fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        description = description,
        image = image,
        price = price,
        isAvailable = isAvailable,
        categoryId = categoryId,
    )


fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = id, name = name, description = description, image = image,
        price = price, categoryId = categoryId, isAvailable = isAvailable
    )

fun ProductEntity.toDomain(): Product =
    Product(
        id = id, name = name, description = description, image = image,
        price = price, categoryId = categoryId, isAvailable = isAvailable
    )