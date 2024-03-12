package ru.unlim1x.shelf.presentation.adapters.listeners


interface ProductRecyclerAdapterListener {
    fun allProductsLoaded()
    fun onProductPressed(id: Int?)
    fun onCategoryPressed(text: String?)
}