package com.example.currencytracking.data.repositories

import com.example.currencytracking.data.model.RateCurrency

interface ICurrencyRepository {

    suspend fun addToFavorite(rateCurrency: RateCurrency)
    suspend fun deleteFavorite(rateCurrency: RateCurrency)
    suspend fun getFavoritesCurrency(): List<String>
    suspend fun getRateCurrencyList(): List<RateCurrency>
}