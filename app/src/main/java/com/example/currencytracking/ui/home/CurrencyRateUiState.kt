package com.example.currencytracking.ui.home

import com.example.currencytracking.data.model.RateCurrency

sealed class CurrencyRateUiState {
    data class Success(val list: List<RateCurrency>):CurrencyRateUiState()
    object Loading :CurrencyRateUiState()
}