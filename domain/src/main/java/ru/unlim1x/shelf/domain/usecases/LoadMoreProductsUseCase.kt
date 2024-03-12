package ru.unlim1x.shelf.domain.usecases

import io.reactivex.rxjava3.core.Observable
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.domain.interfaces.ProductRepository

class LoadMoreProductsUseCase(private val productRepository: ProductRepository) {


    fun execute(skip : Int, limit:Int): Observable<List<ProductDomainShort>> {
        return productRepository.loadProducts(limit=limit, skip=skip)
    }
}