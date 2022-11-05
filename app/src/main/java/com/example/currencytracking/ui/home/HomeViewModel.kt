package com.example.currencytracking.ui.home

import androidx.lifecycle.ViewModel
import com.example.currencytracking.data.model.Event
import com.example.currencytracking.data.model.RateCurrency
import com.example.currencytracking.data.repositories.ICurrencyRepository
import com.example.currencytracking.ui.model.SortingType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
internal open class HomeViewModel
@Inject constructor(
    open val currencyRepository: ICurrencyRepository
) : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    private val _uiState = MutableStateFlow<CurrencyRateUiState>(CurrencyRateUiState.Loading)
    val uiState: StateFlow<CurrencyRateUiState> = _uiState

    private val _eventsClick = MutableStateFlow<Event<CurrencyRateUiEvent?>>(Event(null))
    val eventsClick: StateFlow<Event<CurrencyRateUiEvent?>> = _eventsClick

    protected var ratesUSD: List<RateCurrency>? = null
    private var baseRateCurrency = RateCurrency("USD", 1.0)

    private var currencyList: Array<String>? = null
    private var sortingType: SortingType = SortingType.ALPHABETICALLY

    init {
        launch {
            FavoriteEventBus.changeSortingTypeEvents.collect {
                if (sortingType != it) {
                    sortingType = it
                    onChangeRateCurrencyList()
                }
            }
        }
        launch {
            ratesUSD = currencyRepository.getRateCurrencyList()
            onChangeRateCurrencyList()
            currencyList = ratesUSD?.map { it.currency }?.toTypedArray()
        }
    }

    open fun onRateCurrencyFavoriteSelected(rateCurrency: RateCurrency) {
        launch { currencyRepository.addToFavorite(rateCurrency) }
        launch { FavoriteEventBus.emitAddToFavoriteEvent(rateCurrency) }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    protected fun onChangeRateCurrencyList() {
        launch {
            getRateCurrencyList()
                ?.let { list ->
                    val changedBaseList =
                        list.map { it.copy(rate = it.rate / baseRateCurrency.rate) }
                    val sortedList = when (sortingType) {
                        SortingType.ALPHABETICALLY_DESCENDING -> changedBaseList.sortedByDescending { it.currency }
                        SortingType.BY_VALUE -> changedBaseList.sortedBy { it.rate }
                        SortingType.BY_VALUE_DESCENDING -> changedBaseList.sortedByDescending { it.rate }
                        else -> changedBaseList
                    }
                    _uiState.value = CurrencyRateUiState.Success(sortedList)
                }
        }
    }

    open suspend fun getRateCurrencyList(): List<RateCurrency>? {
        return ratesUSD
    }

    fun onBaseSelect(currency: String) {
        ratesUSD?.find { it.currency == currency }?.let {
            if (it != baseRateCurrency) {
                baseRateCurrency = it
                onChangeRateCurrencyList()
            }
        }
    }

    fun onBaseChangeClick() {
        currencyList?.let {
            _eventsClick.value = Event(CurrencyRateUiEvent.BaseCurrencyClick(it))
        }
    }

    fun onSortingClick() {
        _eventsClick.value = Event(CurrencyRateUiEvent.SortingClick)
    }
}