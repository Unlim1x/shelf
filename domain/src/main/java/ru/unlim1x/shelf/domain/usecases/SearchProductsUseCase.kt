package ru.unlim1x.shelf.domain.usecases

import io.reactivex.rxjava3.core.Observable
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.domain.interfaces.ProductRepository

class SearchProductsUseCase(private val productRepository: ProductRepository) {


    fun execute(text : String): Observable<List<ProductDomainShort>> {
        return productRepository.loadProductsByText(text = text)
    }
}