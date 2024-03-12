package ru.unlim1x.shelf.data.enteties

data class ProductData(
    val id : Int?, val title : String?, val description : String?,
    val price : Float?, val discountPercentage : Float?,
    val rating : Float?, val stock : Int?, val brand : String?,
    val category : String?, val thumbnail : String?,
    val images: List<String?>?
) {}