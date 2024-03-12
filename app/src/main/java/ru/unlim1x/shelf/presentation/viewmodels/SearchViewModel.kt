package ru.unlim1x.shelf.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.domain.usecases.SearchProductsUseCase

class SearchViewModel(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val listOfProducts: MutableLiveData<List<ProductDomainShort>> = MutableLiveData()
    private val errorLD: MutableLiveData<Boolean> = MutableLiveData(false)
    private val nothingLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    private var lastQuery: String = ""


    val productsList: LiveData<List<ProductDomainShort>> get() = listOfProducts
    val errorWhileLoading: LiveData<Boolean> get() = errorLD
    val nothingLoad: LiveData<Boolean> get() = nothingLoaded


    private val disposeBag: CompositeDisposable by lazy { CompositeDisposable() }


    fun searchProducts(text: String) {
        lastQuery = text
        val result = searchProductsUseCase.execute(text)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {


                    listOfProducts.postValue(it)
                    if (it.isEmpty())
                        nothingLoaded.postValue(true)
                    else {
                        nothingLoaded.postValue(false)
                    }


                    Log.e("AAA", "OnNextCall")
                    Log.e("AAA", "${it.size}")
                },
                {
                    errorLD.postValue(true) //Сообщаем посредством ЛД фрагменту, что ошибка при загрузке
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

    fun researchProducts() {
        if (lastQuery.length > 0)
            searchProducts(lastQuery)
    }

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }
}