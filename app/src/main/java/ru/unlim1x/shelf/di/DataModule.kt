package ru.unlim1x.shelf.di

import org.koin.dsl.module
import ru.unlim1x.shelf.data.implementations.ProductRepositoryImpl
import ru.unlim1x.shelf.domain.interfaces.ProductRepository


val dataModule = module {
    single<ProductRepository> {
        ProductRepositoryImpl()
    }


}