package com.example.currencytracking.ui.home

sealed class CurrencyRateUiEvent {
    class BaseCurrencyClick(val array: Array<String>):CurrencyRateUiEvent()
    object SortingClick :CurrencyRateUiEvent()
}