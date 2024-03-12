package ru.unlim1x.shelf.domain.enteties

data class ProductDomainShort(
    val id : Int?, val title : String?, val description : String?,
    val price : Float?, val thumbnail : String?
) {}