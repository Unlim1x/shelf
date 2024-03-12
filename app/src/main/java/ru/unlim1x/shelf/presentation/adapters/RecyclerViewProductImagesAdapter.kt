package ru.unlim1x.shelf.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.unlim1x.shelf.R

class RecyclerViewProductImagesAdapter() :
    RecyclerView.Adapter<RecyclerViewProductImagesAdapter.ViewHolder>() {

    private var urlList: List<String?>? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.product_image_in_rv_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_image_card, parent, false)
        return ViewHolder(card)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("RVIA", "position: $position")
        if (urlList != null) {

            holder.imageView.load(
                urlList!![position]
            ) {
                crossfade(true)

            }

        } else {
            Log.e("RVIA", "empty list onBindViewHolder")
        }

    }

    fun setList(urlList: List<String?>?) {
        this.urlList = urlList
        Log.e("RVIA", "Size of list: ${this.urlList?.size}")
    }


    override fun getItemCount(): Int {
        return if (urlList != null) {
            //Log.e("RVA", "item count called")
            urlList!!.size
        } else
            0
    }


}