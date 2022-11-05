package com.example.currencytracking.data.repositories

import android.content.Context
import androidx.room.Room
import com.example.currencytracking.data.db.AppDatabase
import com.example.currencytracking.data.db.CurrencyEntity
import com.example.currencytracking.data.model.RateCurrency
import com.example.currencytracking.data.service.CurrencyService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CurrencyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val currencyService: CurrencyService,
) : ICurrencyRepository {

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATA_BASE_NAME).build()
    }

    override suspend fun addToFavorite(rateCurrency: RateCurrency) {
        withContext(Dispatchers.IO) {
            db.currencyDao().insert(CurrencyEntity(rateCurrency.currency))
        }
    }

    override suspend fun deleteFavorite(rateCurrency: RateCurrency) {
        withContext(Dispatchers.IO) {
            db.currencyDao().delete(CurrencyEntity(rateCurrency.currency))
        }
    }

    override suspend fun getFavoritesCurrency(): List<String> {
        return withContext(Dispatchers.IO) { db.currencyDao().all.map { it.currency } }
    }

    override suspend fun getRateCurrencyList(): List<RateCurrency> =
        withContext(Dispatchers.IO) {
            currencyService.latest().rates.list
        }
}