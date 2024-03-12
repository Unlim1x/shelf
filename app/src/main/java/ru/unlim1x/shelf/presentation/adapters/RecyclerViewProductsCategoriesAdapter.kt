package ru.unlim1x.shelf.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.unlim1x.shelf.R
import ru.unlim1x.shelf.presentation.adapters.listeners.ProductRecyclerAdapterListener

class RecyclerViewProductsCategoriesAdapter(val productRecyclerAdapterListener: ProductRecyclerAdapterListener) :
    RecyclerView.Adapter<RecyclerViewProductsCategoriesAdapter.ViewHolder>() {

    private var categories: List<String?>? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.category_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_card, parent, false)
        return ViewHolder(card)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("RVCA", "position: $position")
        if (categories != null) {
            holder.textView.text = categories!![position]

        } else {
            Log.e("RVIA", "empty list onBindViewHolder")
        }
        holder.itemView.setOnClickListener {
            productRecyclerAdapterListener.onCategoryPressed(categories!![position])
        }

    }

    fun setList(categories: List<String?>?) {
        this.categories = categories
        Log.e("RVIA", "Size of list: ${this.categories?.size}")
    }


    override fun getItemCount(): Int {
        return if (categories != null) {
            //Log.e("RVA", "item count called")
            categories!!.size
        } else
            0
    }

    private fun resetColors() {

    }


}