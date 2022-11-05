package com.example.currencytracking.ui.sorting

import androidx.lifecycle.ViewModel
import com.example.currencytracking.data.model.Event
import com.example.currencytracking.ui.home.FavoriteEventBus
import com.example.currencytracking.ui.model.SortingType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.coroutines.CoroutineContext

class SortingViewModel : ViewModel(), CoroutineScope {

    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    private val _uiState =
        MutableStateFlow<SortingUiState>(SortingUiState.Success(SortingType.ALPHABETICALLY))
    val uiState: StateFlow<SortingUiState> = _uiState

    private val _eventClose: MutableStateFlow<Event<Unit?>> = MutableStateFlow(Event(null))
    var eventClose: StateFlow<Event<Unit?>> = _eventClose

    init {
        launch(start = CoroutineStart.UNDISPATCHED) {
            FavoriteEventBus.changeSortingTypeEvents.firstOrNull()?.let {
                _uiState.value = SortingUiState.Success(it)
            }

        }
    }

    fun onSortingCheckedChange(sortingType: SortingType?) {
        sortingType ?: return
        launch {
            FavoriteEventBus.emitChangeSortingTypeEvent(sortingType)
            _eventClose.value = Event(Unit)
        }
    }
}