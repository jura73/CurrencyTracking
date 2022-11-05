package com.example.currencytracking.ui.sorting

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.currencytracking.R
import com.example.currencytracking.databinding.FragmentSortingBinding
import com.example.currencytracking.ui.model.SortingType
import com.example.currencytracking.ui.model.SortingType.*
import kotlinx.coroutines.launch

class SortingFragment : DialogFragment() {

    companion object {
        const val TAG = "SortingFragment"
    }

    private var _binding: FragmentSortingBinding? = null

    private val binding get() = _binding!!
    private val sortingViewModel: SortingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSortingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(R.string.sorting)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sortingViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is SortingUiState.Success -> setSortingType(uiState.sortingType)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sortingViewModel.eventClose.collect {
                    it.contentIfNotHandled?.let {
                        this@SortingFragment.dialog?.dismiss()
                    }
                }
            }
        }
    }

    private fun setSortingType(sortingType: SortingType) {
        when (sortingType) {
            ALPHABETICALLY -> binding.rbtAlphabeticallyAscending.isChecked = true
            ALPHABETICALLY_DESCENDING -> binding.rbtAlphabeticallyDescending.isChecked = true
            BY_VALUE -> binding.rbtByValueAscending.isChecked = true
            BY_VALUE_DESCENDING -> binding.rbtByValueDescending.isChecked = true
        }
        binding.rgrSorting.setOnCheckedChangeListener { _, checkedId ->
            val sorting = when (checkedId) {
                R.id.rbtAlphabeticallyAscending -> ALPHABETICALLY
                R.id.rbtAlphabeticallyDescending -> ALPHABETICALLY_DESCENDING
                R.id.rbtByValueAscending -> BY_VALUE
                R.id.rbtByValueDescending -> BY_VALUE_DESCENDING
                else -> null
            }
            sortingViewModel.onSortingCheckedChange(sorting)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}