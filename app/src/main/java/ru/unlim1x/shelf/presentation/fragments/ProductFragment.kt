package ru.unlim1x.shelf.presentation.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.unlim1x.shelf.R
import ru.unlim1x.shelf.presentation.MainActivity
import ru.unlim1x.shelf.presentation.adapters.RecyclerViewProductImagesAdapter
import ru.unlim1x.shelf.presentation.viewmodels.ProductViewModel

class ProductFragment : Fragment() {

    private val viewModel by viewModel<ProductViewModel>()
    private var idProduct: Int? = 0
    private val star = "★ "
    private val dollar = "$ "
    private val off = "% ↘"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).showUpButton()
        idProduct = arguments?.getInt("product")

        val productImagesRecyclerAdapter = RecyclerViewProductImagesAdapter()
        val productImagesRecyclerView = view.findViewById<RecyclerView>(R.id.productImagesRV)
        productImagesRecyclerView.adapter = productImagesRecyclerAdapter

        val brand = view.findViewById<TextView>(R.id.brand)
        val title = view.findViewById<TextView>(R.id.title)
        val discount = view.findViewById<TextView>(R.id.product_discount)
        val raiting = view.findViewById<TextView>(R.id.raiting)
        val price = view.findViewById<TextView>(R.id.product_price)
        val description = view.findViewById<TextView>(R.id.product_description)

        brand.visibility = View.INVISIBLE
        title.visibility = View.INVISIBLE
        discount.visibility = View.INVISIBLE
        raiting.visibility = View.INVISIBLE
        price.visibility = View.INVISIBLE
        description.visibility = View.INVISIBLE

        val repeatButton = view.findViewById<Button>(R.id.repeat_button)
        val progressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)
        val errorCard = view.findViewById<CardView>(R.id.error_card)
        errorCard.visibility = View.INVISIBLE

        viewModel.productLD.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
            errorCard.visibility = View.GONE
            brand.text = it.brand
            title.text = it.title
            discount.text = StringBuilder(it.discountPercentage.toString()).append(off)
            raiting.text = StringBuilder(star).append(it.rating.toString())
            price.text = StringBuilder(dollar).append(it.price.toString())
            description.text = it.description
            brand.visibility = View.VISIBLE
            title.visibility = View.VISIBLE
            discount.visibility = View.VISIBLE
            raiting.visibility = View.VISIBLE
            price.visibility = View.VISIBLE
            description.visibility = View.VISIBLE


            productImagesRecyclerAdapter.setList(it.images)
            it.images?.size?.let { it1 ->
                productImagesRecyclerAdapter.notifyItemRangeChanged(
                    0,
                    it1
                )
            }
        }

        if (viewModel.productLD.value == null) {
            progressBar.visibility = View.VISIBLE
            viewModel.loadProduct(idProduct!!)
        }

        viewModel.errorWhileLoading.observe(viewLifecycleOwner) {
            if (it && viewModel.productLD.value == null) {
                errorCard.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }

        repeatButton.setOnClickListener {
            errorCard.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            viewModel.loadProduct(idProduct!!)
        }



        super.onViewCreated(view, savedInstanceState)
    }
}