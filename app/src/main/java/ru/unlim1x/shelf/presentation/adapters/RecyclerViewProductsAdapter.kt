package ru.unlim1x.shelf.presentation.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import ru.unlim1x.shelf.R
import ru.unlim1x.shelf.domain.enteties.ProductDomainShort
import ru.unlim1x.shelf.presentation.adapters.listeners.ProductRecyclerAdapterListener


class RecyclerViewProductsAdapter(val productRecyclerAdapterListener: ProductRecyclerAdapterListener) :
    RecyclerView.Adapter<RecyclerViewProductsAdapter.ViewHolder>() {

    private var image: Pair<Int?, Drawable?>? = Pair(null, null)
    private var productsList: List<ProductDomainShort?>? = null
    private var dollar = "$ "

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: Int? = null
        var product_name: TextView = itemView.findViewById(R.id.product_name)
        var product_watering: TextView = itemView.findViewById(R.id.product_watering)
        var product_description: TextView = itemView.findViewById(R.id.product_description)
        var product_image: ImageView = itemView.findViewById(R.id.product_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(card)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("RVA", "position: $position")
        if (productsList != null) {
            holder.id = productsList!![position]?.id
            holder.product_name.text = productsList!![position]?.title
            Log.e("RVA product, position", "${productsList!![position]?.title}, $position")
            val watering = StringBuilder(dollar)
            val period = productsList!![position]?.price
            watering.append(period)

            holder.product_watering.text = watering.toString()
            holder.product_description.text = productsList!![position]?.description



            holder.product_image.load(
                productsList!![position]?.thumbnail
            ) {
                crossfade(true)
                transformations(RoundedCornersTransformation(20f))
            }

            if (productsList!!.size == position + 1)
                productRecyclerAdapterListener.allProductsLoaded()

        } else {
            Log.e("RVA", "empty list onBindViewHolder")
        }
        holder.itemView.setOnClickListener {
            productRecyclerAdapterListener.onProductPressed(holder.id)
        }
    }

    fun setList(productsList: List<ProductDomainShort?>?) {
        this.productsList = productsList
        Log.e("RVA", "Size of list: ${this.productsList?.size}")
    }


    override fun getItemCount(): Int {
        return if (productsList != null) {
            //Log.e("RVA", "item count called")
            productsList!!.size
        } else
            0
    }


}