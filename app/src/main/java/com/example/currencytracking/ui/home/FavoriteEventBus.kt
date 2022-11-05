package com.example.currencytracking.ui.home

import com.example.currencytracking.data.model.RateCurrency
import com.example.currencytracking.ui.model.SortingType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object FavoriteEventBus {

    private val _addToFavoriteEvents = MutableSharedFlow<RateCurrency>()
    val addToFavoriteEvents = _addToFavoriteEvents.asSharedFlow()

    suspend fun emitAddToFavoriteEvent(rateCurrency: RateCurrency) {
        _addToFavoriteEvents.emit(rateCurrency)
    }

    private val _changeSortingTypeEvents = MutableSharedFlow<SortingType>(replay = 1)
    val changeSortingTypeEvents = _changeSortingTypeEvents.asSharedFlow()

    suspend fun emitChangeSortingTypeEvent(sortingType: SortingType) {
        _changeSortingTypeEvents.emit(sortingType)
    }

}