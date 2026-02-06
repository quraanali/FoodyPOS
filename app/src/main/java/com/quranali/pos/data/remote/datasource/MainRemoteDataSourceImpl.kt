package com.quranali.pos.data.remote.datasource

import com.quranali.pos.data.remote.dto.CategoryDto
import com.quranali.pos.data.remote.dto.ProductDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.CancellationException


class MainRemoteDataSourceImpl(private val client: HttpClient) : MainRemoteDataSource {
    override suspend fun refreshCategories(): List<CategoryDto> {
        return try {
            client.get("/categories.json") {
                header(HttpHeaders.Accept, ContentType.Application.Json)

            }.body<List<CategoryDto>>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun refreshProducts(): List<ProductDto> {
        return try {
            client.get("/products.json") {
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }.body<List<ProductDto>>()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            emptyList()
        }
    }
}
