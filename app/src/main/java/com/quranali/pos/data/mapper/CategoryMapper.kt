package com.quranali.pos.data.mapper

import com.quranali.pos.data.local.entity.CategoryEntity
import com.quranali.pos.data.remote.dto.CategoryDto
import com.quranali.pos.domain.model.Category


fun CategoryEntity.toDomain() = Category(id, name)

fun Category.toEntity() = CategoryEntity(id, name)

fun CategoryDto.toDomain() = Category(id, name)