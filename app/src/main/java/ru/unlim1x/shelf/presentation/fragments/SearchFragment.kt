package ru.unlim1x.shelf.presentation.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.unlim1x.shelf.R
import ru.unlim1x.shelf.presentation.MainActivity
import ru.unlim1x.shelf.presentation.adapters.RecyclerViewProductsAdapter
import ru.unlim1x.shelf.presentation.adapters.listeners.ProductRecyclerAdapterListener
import ru.unlim1x.shelf.presentation.viewmodels.SearchViewModel
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(), ProductRecyclerAdapterListener {

    private val viewModel by viewModel<SearchViewModel>()
    private val disposeBag: CompositeDisposable by lazy { CompositeDisposable() }
    private lateinit var navController: NavController
    private lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).showUpButton()
        navController = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment_container
        )


        val productRecyclerAdapter = RecyclerViewProductsAdapter(this)
        val productsRecyclerView = view.findViewById<RecyclerView>(R.id.products_rv)
        productsRecyclerView.adapter = productRecyclerAdapter
        val repeatButton = view.findViewById<Button>(R.id.repeat_button)
        val progressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)
        progressBar.visibility = View.INVISIBLE
        val errorCard = view.findViewById<CardView>(R.id.error_card)
        val nothingLoadedText = view.findViewById<TextView>(R.id.nothing_loaded_tv)

        viewModel.productsList.observe(viewLifecycleOwner) {//отслеживаем загрузку искомых товаров
            nothingLoadedText.visibility = View.INVISIBLE
            productRecyclerAdapter.setList(it)
            progressBar.visibility = View.INVISIBLE
            errorCard.visibility = View.INVISIBLE
            productRecyclerAdapter.notifyDataSetChanged()

        }

        viewModel.nothingLoad.observe(viewLifecycleOwner) {//обрабатываем <<пустой>> запрос
            if (viewModel.productsList.value?.isEmpty() == true) {
                nothingLoadedText.visibility = View.VISIBLE
            }
        }

        viewModel.errorWhileLoading.observe(viewLifecycleOwner) {
            if (it) {
                errorCard.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }

        repeatButton.setOnClickListener {
            errorCard.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            viewModel.researchProducts()
        }


        searchView = view.findViewById(R.id.searchViewInFragment)

        //Чтобы не искать после ввода каждой буквы, используется rxbinding для searchview и как раз-таки debounce
        val a = (searchView.queryTextChanges()
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    viewModel.searchProducts(it.toString())
                    progressBar.visibility = View.VISIBLE
                    Log.e("OBS", it.toString())
                }
            },
                {
                    Log.e("OBS", it.stackTraceToString())
                }))
        searchView.isIconified = false
        disposeBag.add(a)
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onProductPressed(id: Int?) { //Открываем подробно товар
        id?.let {
            val productBundle = Bundle()
            productBundle.putInt("product", id)
            navController.navigate(R.id.action_searchFragment_to_productFragment, productBundle)
        }
    }

    override fun allProductsLoaded() {
    }

    override fun onCategoryPressed(text: String?) {
    }

}