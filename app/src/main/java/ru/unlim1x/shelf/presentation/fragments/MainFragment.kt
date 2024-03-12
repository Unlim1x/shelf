package ru.unlim1x.shelf.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.unlim1x.shelf.R
import ru.unlim1x.shelf.presentation.MainActivity
import ru.unlim1x.shelf.presentation.adapters.RecyclerViewProductsAdapter
import ru.unlim1x.shelf.presentation.adapters.RecyclerViewProductsCategoriesAdapter
import ru.unlim1x.shelf.presentation.adapters.listeners.ProductRecyclerAdapterListener
import ru.unlim1x.shelf.presentation.viewmodels.MainViewModel

class MainFragment : Fragment(), ProductRecyclerAdapterListener {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var navController: NavController
    private lateinit var menuHost: MenuHost
    private var fullLoad: Boolean = false
    private var elements_loaded_first_time: Boolean = true
    private var fragmentJustCreated = true
    private var oldSize: Int = 0
    private var categorized: Boolean = false
    private var lastCategory: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as MainActivity).hideUpButton()
        fragmentJustCreated = true
        navController = Navigation.findNavController(
            requireActivity(),
            R.id.nav_host_fragment_container
        )

        val scrollView = view.findViewById<NestedScrollView>(R.id.main)

        val productRecyclerAdapter = RecyclerViewProductsAdapter(this)
        val productsRecyclerView = view.findViewById<RecyclerView>(R.id.products_rv)
        productsRecyclerView.adapter = productRecyclerAdapter

        val categoriesRecyclerAdapter = RecyclerViewProductsCategoriesAdapter(this)
        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categories_rv)
        categoriesRecyclerView.adapter = categoriesRecyclerAdapter

        val repeatButton = view.findViewById<Button>(R.id.repeat_button)
        val progressBar = view.findViewById<ProgressBar>(R.id.loading_progress_bar)
        val errorCard = view.findViewById<CardView>(R.id.error_card)

        //Отслеживание загрузки товаров
        viewModel.productsList.observe(viewLifecycleOwner) {
            if (!categorized) {
                productRecyclerAdapter.setList(it)
                progressBar.visibility = View.GONE
                errorCard.visibility = View.GONE
                productRecyclerAdapter.notifyItemRangeChanged(oldSize, it.size)
                oldSize = it.size
            } else {
                productRecyclerAdapter.setList(it)
                progressBar.visibility = View.GONE
                errorCard.visibility = View.GONE
                productRecyclerAdapter.notifyDataSetChanged()
            }
        }
        //Старт загрузки
        if (viewModel.productsList.value.isNullOrEmpty()) {
            progressBar.visibility = View.VISIBLE
            viewModel.loadMoreProducts()
        }
        //Если ошибка, то показываем
        viewModel.errorWhileLoading.observe(viewLifecycleOwner) {
            if (it) {
                errorCard.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }
//Загрузка категорий
        viewModel.loadCategories()

        viewModel.categoriesList.observe(viewLifecycleOwner) {
            categoriesRecyclerAdapter.setList(it)
            categoriesRecyclerAdapter.notifyItemRangeChanged(0, it.size)
        }
//Обработка нажатия кнопки "повторить" при ошибке загрузки
        repeatButton.setOnClickListener {
            errorCard.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            viewModel.loadCategories()
            if (!categorized)
                viewModel.loadMoreProducts()
            else
                viewModel.loadProductsByCategory(lastCategory)
        }
//Отслеживание прокрутки до конца списка, чтобы дозагрузить новые товары
        scrollView?.viewTreeObserver?.addOnScrollChangedListener {
            if (scrollView.getChildAt(0).bottom
                == (scrollView.height + scrollView.scrollY)
            ) {
                if (!fullLoad) {
                    progressBar.visibility = View.VISIBLE
                    if (categorized)
                        viewModel.loadProductsByCategory(lastCategory)
                    else
                        viewModel.loadMoreProducts()
                } else
                    progressBar.visibility = View.GONE
            }
        }

        viewModel.fullLoad.observe(viewLifecycleOwner) {
            if (viewModel.fullLoad.value == true) {
                fullLoad = true
                progressBar.visibility = View.GONE
            }
        }

        //Меню хост для кнопки поиска в action bar'е
        menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.app_bar_search) {
                    navController.navigate(R.id.action_mainFragment_to_searchFragment)
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.CREATED)

        super.onViewCreated(view, savedInstanceState)
    }
    //Когда мы внизу списка товаров, и загрузились новые, поднимаем немного scrollview, чтобы увидеть, что там есть ещё.
    override fun allProductsLoaded() {
        val scrollView = view?.findViewById<NestedScrollView>(R.id.main)
        if (!elements_loaded_first_time && !fragmentJustCreated)
            scrollView?.post {
                scrollView.smoothScrollTo(
                    scrollView.x.toInt(),
                    scrollView.scrollY + 70
                )
            }
        elements_loaded_first_time = false
        fragmentJustCreated = false
    }

    override fun onProductPressed(id: Int?) {
        id?.let {
            val productBundle = Bundle()
            productBundle.putInt("product", id)
            navController.navigate(R.id.action_mainFragment_to_productFragment, productBundle)
        }

    }


    override fun onCategoryPressed(text: String?) {
        if (text.equals("All")) {
            if (categorized) {
                viewModel.resetData()
                viewModel.loadMoreProducts()
            }
            categorized = false
        } else {
            categorized = true
            if (text != null) {
                lastCategory = text
                viewModel.resetData()
                viewModel.loadProductsByCategory(text)
            }
        }
    }
}