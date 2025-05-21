package com.spau.rwc.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
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

    private fun loadData() {
        viewModel.loadRestaurants()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.restaurants.collect { restaurants ->
                    adapter.submitList(restaurants)
                }
            }
        }
    }

}