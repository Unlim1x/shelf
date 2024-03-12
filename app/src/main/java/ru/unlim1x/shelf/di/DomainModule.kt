package ru.unlim1x.shelf.di

import org.koin.dsl.module
import ru.unlim1x.shelf.domain.usecases.LoadCategoriesUseCase
import ru.unlim1x.shelf.domain.usecases.LoadMoreProductsUseCase
import ru.unlim1x.shelf.domain.usecases.LoadProductByIdUseCase
import ru.unlim1x.shelf.domain.usecases.LoadProductsByCategoriesUseCase
import ru.unlim1x.shelf.domain.usecases.SearchProductsUseCase


val domainModule = module {
    factory<LoadMoreProductsUseCase> {
        LoadMoreProductsUseCase(productRepository = get())
    }

    factory<LoadProductByIdUseCase> {
        LoadProductByIdUseCase(productRepository = get())
    }

    factory<SearchProductsUseCase> {
        SearchProductsUseCase(productRepository = get())
    }
    factory<LoadCategoriesUseCase> {
        LoadCategoriesUseCase(productRepository = get())
    }
    factory<LoadProductsByCategoriesUseCase> {
        LoadProductsByCategoriesUseCase(productRepository = get())
    }

}