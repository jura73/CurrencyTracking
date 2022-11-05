package com.example.currencytracking.ui.sorting

import com.example.currencytracking.ui.model.SortingType

sealed class SortingUiState {
    data class Success(val sortingType: SortingType) : SortingUiState()
}