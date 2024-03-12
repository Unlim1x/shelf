package ru.unlim1x.shelf.domain.usecases

import io.reactivex.rxjava3.core.Observable
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.domain.interfaces.ProductRepository

class LoadProductsByCategoriesUseCase(private val productRepository: ProductRepository) {
    fun execute(category:String, skip:Int, limit:Int): Observable<List<ProductDomainShort>> {
        return productRepository.loadProductsByCategories(category=category, skip=skip, limit = limit)
    }
}