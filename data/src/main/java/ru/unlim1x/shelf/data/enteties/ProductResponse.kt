package ru.unlim1x.shelf.data.enteties


class ProductResponse {
    val products : MutableList<ProductDataShort> = mutableListOf()
    val total :Int? = null //Это и последующие поля не пригодились, но они есть в json'е
    val skip :Int? = null
    val limit :Int? = null
}