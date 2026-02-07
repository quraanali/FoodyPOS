package com.quranali.pos.di


import com.quranali.pos.domain.usecase.GetProductsByCategoryIdUseCase
import com.quranali.pos.domain.usecase.GetSearchProductsByNameUseCase
import com.quranali.pos.domain.usecase.ObserveAllProductsUseCase
import com.quranali.pos.domain.usecase.ObserveCategoriesUseCase
import com.quranali.pos.domain.usecase.RefreshAllProductsUseCase
import com.quranali.pos.domain.usecase.RefreshCategoriesUseCase
import org.koin.dsl.module

val commonDomainModule = module {

    factory {
        RefreshAllProductsUseCase(get())
    }

    factory {
        ObserveCategoriesUseCase(get())
    }

    factory {
        ObserveAllProductsUseCase(get())
    }


    factory {
        RefreshCategoriesUseCase(get())
    }

    factory {
        GetSearchProductsByNameUseCase(get())
    }

    factory {
        GetProductsByCategoryIdUseCase(get())
    }

}
