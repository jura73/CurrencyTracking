package com.example.currencytracking.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencytracking.R
import com.example.currencytracking.databinding.FragmentHomeBinding
import com.example.currencytracking.ui.sorting.SortingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal open class HomeFragment : Fragment() {

    //todo
    //Kotlin +, MVVM +, StateFlow +, Coroutines+, Room+, Dagger2
    // firs screen +
    // select base currency +
    // favorite screen +
    // favorite StateFlow
    // save favorite to DB +
    // filter screen +
    // filter home +
    //  filter favorite +

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    open val homeViewModel: HomeViewModel by viewModels()

    private val usersAdapter = RateCurrencyAdapter {
        homeViewModel.onRateCurrencyFavoriteSelected(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBase.setOnClickListener {
            homeViewModel.onBaseChangeClick()
        }
        binding.imgbSorting.setOnClickListener {
            homeViewModel.onSortingClick()
        }
        binding.rcvUsers.adapter = usersAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is CurrencyRateUiState.Success -> {
                            usersAdapter.list = uiState.list
                            usersAdapter.notifyDataSetChanged()
                            binding.prbLoading.isVisible = false
                        }
                        is CurrencyRateUiState.Loading -> binding.prbLoading.isVisible = true
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.eventsClick.collect { event ->
                    event.contentIfNotHandled?.let {
                        when (it) {
                            is CurrencyRateUiEvent.BaseCurrencyClick -> showSelectBaseCurrency(it.array)
                            is CurrencyRateUiEvent.SortingClick -> showSortingDialog()
                        }
                    }
                }

            }
        }
    }

    private fun showSelectBaseCurrency(arrayCurrency: Array<String>) {
        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(R.string.select_currency)
            .setItems(arrayCurrency) { dialog, which ->
                homeViewModel.onBaseSelect(arrayCurrency[which])
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showSortingDialog() {
        SortingFragment().show(childFragmentManager, SortingFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}