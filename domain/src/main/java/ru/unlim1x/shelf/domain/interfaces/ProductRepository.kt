package ru.unlim1x.shelf.domain.interfaces

import io.reactivex.rxjava3.core.Observable
import ru.unlim1x.shelf.domain.enteties.ProductDomain
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort

interface ProductRepository {
    fun loadProducts(limit:Int, skip:Int):Observable<List<ProductDomainShort>>
    fun loadProductById(id:Int):Observable<ProductDomain>
    fun loadProductsByText(text:String):Observable<List<ProductDomainShort>>
    fun loadCategories():Observable<List<String>>
    fun loadProductsByCategories(category: String, skip:Int, limit: Int):Observable<List<ProductDomainShort>>
}