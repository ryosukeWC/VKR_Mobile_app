package com.spau.rwc.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.spau.rwc.R
import com.spau.rwc.databinding.HomeBinding
import com.spau.rwc.model.Restaurant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: HomeBinding
    private lateinit var adapter: RestaurantAdapter
    private val viewModel: RestaurantViewModel by viewModels()
    private var originalRestaurants = emptyList<Restaurant>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        loadData()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = RestaurantAdapter().apply {
            setOnItemClickListener { restaurant ->
                val bundle = Bundle().apply {
                    putParcelable("restaurant", restaurant)
                }
                findNavController().navigate(R.id.action_homeFragment_to_restaurnatDetails, bundle)
            }
        }

        binding.listRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }

    private fun setupSearchView() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterRestaurants(newText.orEmpty())
                return true
            }
        })
    }

    private fun filterRestaurants(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalRestaurants
        } else {
            originalRestaurants.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }
        adapter.submitList(filteredList)
    }

    private fun loadData() {
        viewModel.loadRestaurants()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.restaurants.collect { restaurants ->
                    originalRestaurants = restaurants
                    adapter.submitList(restaurants)
                }
            }
        }
    }
}