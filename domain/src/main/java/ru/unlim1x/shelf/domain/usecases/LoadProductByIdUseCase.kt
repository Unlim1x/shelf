package ru.unlim1x.shelf.domain.usecases

import io.reactivex.rxjava3.core.Observable
import ru.unlim1x.shelf.domain.enteties.ProductDomain
import ru.unlim1x.shelf.domain.interfaces.ProductRepository

class LoadProductByIdUseCase(private val productRepository: ProductRepository) {
    fun execute(id:Int):Observable<ProductDomain>{
            return productRepository.loadProductById(id=id)
    }
}