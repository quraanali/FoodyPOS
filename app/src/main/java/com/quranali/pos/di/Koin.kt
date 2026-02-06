package com.quranali.pos.di

import android.content.Context
import androidx.room.Room
import com.quranali.pos.data.local.database.PosDao
import com.quranali.pos.data.local.database.PosDatabase
import com.quranali.pos.data.local.datasource.MainLocalDataSource
import com.quranali.pos.data.local.datasource.MainLocalDataSourceImpl
import com.quranali.pos.data.remote.datasource.MainRemoteDataSource
import com.quranali.pos.data.remote.datasource.MainRemoteDataSourceImpl
import com.quranali.pos.data.remote.network.HttpClientFactory
import com.quranali.pos.domain.repository.MainRepository
import com.quranali.pos.data.repository.MainRepositoryImpl
import com.quranali.pos.screens.home.HomeViewModel
import com.quranali.pos.screens.splash.SplashViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {

    single { OkHttp.create() }

    single<HttpClient> { HttpClientFactory(get()).create() }
    single<MainRemoteDataSource> {
        MainRemoteDataSourceImpl(get())
    }

    single<MainLocalDataSource> {
        MainLocalDataSourceImpl(get())
    }

    single<MainRepository> {
        MainRepositoryImpl(
            remote = get(),
            local = get()
        )
    }

    single<PosDatabase> {
        Room.databaseBuilder(
            androidContext(),
            PosDatabase::class.java,
            "pos_database"
        )
            .build()
    }

    single<PosDao> {
        get<PosDatabase>().posDao()
    }
}

val viewModelModule = module {
    factoryOf(::HomeViewModel)
    factoryOf(::SplashViewModel)
}

fun initKoin(app: Context) {
    startKoin {
        androidContext(app)
        modules(
            dataModule,
            viewModelModule,
            commonDomainModule
        )
    }
}
