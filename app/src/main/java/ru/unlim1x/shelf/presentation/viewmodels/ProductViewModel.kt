package ru.unlim1x.shelf.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.unlim1x.shelf.domain.enteties.ProductDomain
import ru.unlim1x.shelf.domain.usecases.LoadProductByIdUseCase

class ProductViewModel(private val loadProductByIdUseCase: LoadProductByIdUseCase) : ViewModel() {

    private val product: MutableLiveData<ProductDomain> = MutableLiveData()
    private val errorLD: MutableLiveData<Boolean> = MutableLiveData(false)
    private val disposeBag: CompositeDisposable by lazy { CompositeDisposable() }

    val productLD: LiveData<ProductDomain> get() = product
    val errorWhileLoading: LiveData<Boolean> get() = errorLD

    fun loadProduct(id: Int) {
        val result = loadProductByIdUseCase.execute(id = id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe(
                {
                    product.postValue(it)

                    Log.e("AAA", "OnNextCall")
                    Log.e("AAA", "${it}")
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

    override fun onCleared() {
        disposeBag.dispose()
        super.onCleared()
    }

}