package com.example.currencytracking.data.model

data class LatestModel(val rates: Rates)

data class Rates( val list: List<RateCurrency>)