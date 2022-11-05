package com.example.currencytracking.data.db

import androidx.room.*

@Dao
interface CurrencyDao {
    @get:Query("SELECT * FROM ${AppDatabase.DATA_BASE_NAME}")
    val all: List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(currency: CurrencyEntity)

    @Update
    fun update(currency: CurrencyEntity)

    @Delete
    fun delete(currency: CurrencyEntity)
}