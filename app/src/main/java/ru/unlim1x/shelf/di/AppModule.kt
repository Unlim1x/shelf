package ru.unlim1x.shelf.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.unlim1x.shelf.presentation.viewmodels.MainViewModel
import ru.unlim1x.shelf.presentation.viewmodels.ProductViewModel
import ru.unlim1x.shelf.presentation.viewmodels.SearchViewModel


val appModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            loadMoreProductsUseCase = get(),
            loadCategoriesUseCase = get(),
            loadProductsByCategoriesUseCase = get()
        )
    }

    viewModel<ProductViewModel> {
        ProductViewModel(
            loadProductByIdUseCase = get()
        )
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            searchProductsUseCase = get()
        )
    }

}