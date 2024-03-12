package ru.unlim1x.shelf.data.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.unlim1x.shelf.data.enteties.ProductData
import ru.unlim1x.shelf.data.enteties.ProductResponse

interface RetrofitQueue {

    //Запрос получения товаров с ограничениями по количеству
    @GET("/products")
    fun loadProducts(@Query("skip") skip : Int,
                     @Query("limit") limit : Int): Observable<ProductResponse>

    //Запрос получения товара по id
    @GET("/products/{id}")
    fun loadProductById(@Path("id")id:Int): Observable<ProductData>

    //Запрос поиска товара по совпадениям в текстах
    @GET("/products/search")
    fun searchProducts(@Query("q") text : String): Observable<ProductResponse>

    //Запрос получения категорий
    @GET("/products/categories")
    fun getCategories(): Observable<List<String>>

    //Запрос получения товаров по категории с ограничениями по количеству
    @GET("/products/category/{category}")
    fun loadProductsByCategory(@Path("category")text:String,@Query("skip") skip : Int,
                               @Query("limit") limit : Int): Observable<ProductResponse>
}