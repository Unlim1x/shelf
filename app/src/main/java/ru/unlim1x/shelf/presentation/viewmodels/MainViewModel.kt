package ru.unlim1x.shelf.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.domain.usecases.LoadCategoriesUseCase
import ru.unlim1x.shelf.domain.usecases.LoadMoreProductsUseCase
import ru.unlim1x.shelf.domain.usecases.LoadProductsByCategoriesUseCase

class MainViewModel(
    private val loadMoreProductsUseCase: LoadMoreProductsUseCase,
    private val loadCategoriesUseCase: LoadCategoriesUseCase,
    private val loadProductsByCategoriesUseCase: LoadProductsByCategoriesUseCase
) : ViewModel() {

    private val listOfProducts: MutableLiveData<List<ProductDomainShort>> = MutableLiveData()
    private val listOfCategories: MutableLiveData<List<String>> = MutableLiveData()
    private val errorLD: MutableLiveData<Boolean> = MutableLiveData(false)
    private val allLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var skip: Int = 0 //Для скипа уже загруженных -> в query запрос
    private val limit = 20 //"По 20 товаров"


    val productsList: LiveData<List<ProductDomainShort>> get() = listOfProducts
    val categoriesList: LiveData<List<String>> get() = listOfCategories
    val errorWhileLoading: LiveData<Boolean> get() = errorLD
    val fullLoad: LiveData<Boolean> get() = allLoaded


    private val disposeBag: CompositeDisposable by lazy { CompositeDisposable() }


    fun loadMoreProducts() { //Подгрузка товаров
        val result = loadMoreProductsUseCase.execute(skip, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {
                    skip += 20
                    if (listOfProducts.value.isNullOrEmpty())
                        listOfProducts.postValue(it)
                    else {
                        if (it.isEmpty()) {
                            allLoaded.postValue(true)
                        } else {
                            val productList: MutableList<ProductDomainShort> = mutableListOf()
                            productList.addAll(listOfProducts.value!!)
                            productList.addAll(it)
                            listOfProducts.postValue(productList)
                        }
                    }

                    Log.e("AAA", "OnNextCall")
                    Log.e("AAA", "${it.size}")
                },
                {
                    errorLD.postValue(true)      //Сообщаем посредством ЛД фрагменту, что ошибка при загрузке
                    Log.e("AAA", "Error, loading is incomplete")
                    Log.e("AAA", "${it.stackTraceToString()}")
                },
                {
                    errorLD.postValue(false)
                    Log.e("AAA", "Loading is complete")
                }
            )

        disposeBag.add(result)

    }

    fun loadCategories() { //Для загрузки категорий
        val result = loadCategoriesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {

                    listOfCategories.postValue(it)

                    Log.e("AAA", "OnNextCall")
                    Log.e("AAA", "${it.size}")
                },
                {
                    errorLD.postValue(true)
                    Log.e("AAA", "Error, loading is incomplete")
                    Log.e("AAA", it.stackTraceToString())
                },
                {
                    errorLD.postValue(false)
                    Log.e("AAA", "Loading is complete")
                }
            )

        disposeBag.add(result)

    }

    fun loadProductsByCategory(categoryName: String) { //Грузит товары по выбранной категории
        if (categoryName.length > 0) {
            val result = loadProductsByCategoriesUseCase.execute(categoryName, skip, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                    {
                        skip += 20
                        if (listOfProducts.value.isNullOrEmpty())
                            listOfProducts.postValue(it)
                        else {
                            if (it.isEmpty()) {
                                allLoaded.postValue(true)
                            } else {
                                val productList: MutableList<ProductDomainShort> = mutableListOf()
                                productList.addAll(listOfProducts.value!!)
                                productList.addAll(it)
                                listOfProducts.postValue(productList)
                            }
                        }

                        Log.e("AAA", "OnNextCall")
                        Log.e("AAA", "${it.size}")
                    },
                    {
                        errorLD.postValue(true)
                        Log.e("AAA", "Error, loading is incomplete")
                        Log.e("AAA", "${it.stackTraceToString()}")
                    },
                    {
                        errorLD.postValue(false)
                        Log.e("AAA", "Loading is complete")
                    }
                )

            disposeBag.add(result)

        }
    }

    fun resetData() {
        skip = 0
        listOfProducts.value = listOf()

    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }
}