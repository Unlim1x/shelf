package ru.unlim1x.shelf.data.implementations

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import ru.unlim1x.shelf.data.retrofit.RetrofitClient
import ru.unlim1x.shelf.domain.enteties.ProductDomain
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.domain.interfaces.ProductRepository

class ProductRepositoryImpl : ProductRepository {
    override fun loadProducts(limit: Int, skip: Int): Observable<List<ProductDomainShort>> {
        val observableListProductDomainShort = RetrofitClient.get().loadProducts(skip = skip, limit = limit)
            .map { it ->

                val mutableList: MutableList<ProductDomainShort> = mutableListOf()
                it.products.forEach {
                    Log.e("AAA", "Mapping ${it.title}")
                    val product = ProductDomainShort(
                        it.id, it.title, it.description, it.price, it.thumbnail
                    )
                    mutableList.add(product)
                }
                val b: List<ProductDomainShort> = mutableList
                b
            }
        return observableListProductDomainShort
    }

    override fun loadProductById(id: Int): Observable<ProductDomain> {
        val observableProductData = RetrofitClient.get().loadProductById(id)
            .map{it->
                val productDomain = ProductDomain(
                    it.id, it.title, it.description, it.price, it.discountPercentage, it.rating,
                    it.stock, it.brand, it.category, it.thumbnail, it.images
                )
                productDomain
            }
        return observableProductData
    }

    override fun loadProductsByText(text: String): Observable<List<ProductDomainShort>> {
        val observableListProductDomainShort = RetrofitClient.get().searchProducts(text)
            .map { it ->

                val mutableList: MutableList<ProductDomainShort> = mutableListOf()
                it.products.forEach {
                    Log.e("AAA", "Mapping ${it.title}")
                    val product = ProductDomainShort(
                        it.id, it.title, it.description, it.price, it.thumbnail
                    )
                    mutableList.add(product)
                }
                val b: List<ProductDomainShort> = mutableList
                b
            }
        return observableListProductDomainShort
    }

    override fun loadCategories(): Observable<List<String>> {
        return RetrofitClient.get().getCategories().map {
            val list :MutableList<String> = mutableListOf()
            list.add("All")
            it.forEach { it1->list.add(it1) }
            list
        }
    }

    override fun loadProductsByCategories(category: String, skip:Int, limit: Int): Observable<List<ProductDomainShort>> {
        val observableListProductDomainShort = RetrofitClient.get().loadProductsByCategory(category,skip,limit)
            .map { it ->

                val mutableList: MutableList<ProductDomainShort> = mutableListOf()
                it.products.forEach {
                    Log.e("AAA", "Mapping ${it.title}")
                    val product = ProductDomainShort(
                        it.id, it.title, it.description, it.price, it.thumbnail
                    )
                    mutableList.add(product)
                }
                val b: List<ProductDomainShort> = mutableList
                b
            }
        return observableListProductDomainShort
    }
}