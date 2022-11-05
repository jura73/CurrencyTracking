package com.example.currencytracking.ui.favorites

import com.example.currencytracking.data.model.RateCurrency
import com.example.currencytracking.data.repositories.ICurrencyRepository
import com.example.currencytracking.ui.home.FavoriteEventBus
import com.example.currencytracking.ui.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal open class FavoritesViewModel @Inject constructor(
    override val currencyRepository: ICurrencyRepository
) : HomeViewModel(currencyRepository) {

    private var favoritesCurrency: MutableSet<String> = mutableSetOf()

    init {
        launch { favoritesCurrency.addAll(currencyRepository.getFavoritesCurrency()) }

        launch {
            FavoriteEventBus.addToFavoriteEvents.collect {
                if (favoritesCurrency.contains(it.currency).not()) {
                    favoritesCurrency.add(it.currency)
                    onChangeRateCurrencyList()
                }
            }
        }
    }

    override fun onRateCurrencyFavoriteSelected(rateCurrency: RateCurrency) {
        launch { currencyRepository.deleteFavorite(rateCurrency) }
        favoritesCurrency.remove(rateCurrency.currency)
        onChangeRateCurrencyList()
    }

    override suspend fun getRateCurrencyList(): List<RateCurrency>? {
        return ratesUSD?.filter { favoritesCurrency.contains(it.currency) }
    }

}