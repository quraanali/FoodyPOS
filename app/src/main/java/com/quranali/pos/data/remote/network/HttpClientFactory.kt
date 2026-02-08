package com.quranali.pos.data.remote.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val engine: HttpClientEngine
) {
    fun create(): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                        prettyPrint = true
                    },
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 25000L
                requestTimeoutMillis = 25000L
            }

            defaultRequest {
                url("https://my.api.mockaroo.com")
                contentType(ContentType.Application.Json)
                header("X-API-Key", "1dbffa50")
            }

//            install(Logging) {
//                level = LogLevel.ALL
//
//                logger = object : io.ktor.client.plugins.logging.Logger {
//                    override fun log(message: String) {
//                        Log.d(
//                            "AliPosLog",
//                            message
//                        )
//                    }
//                }
//            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }

        }
    }
}