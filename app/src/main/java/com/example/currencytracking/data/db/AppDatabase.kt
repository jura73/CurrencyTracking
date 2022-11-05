package com.example.currencytracking.data.db

import androidx.room.Database
import com.example.currencytracking.data.db.CurrencyEntity
import androidx.room.RoomDatabase
import com.example.currencytracking.data.db.CurrencyDao

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATA_BASE_NAME = "CurrencyEntity"
    }
    abstract fun currencyDao(): CurrencyDao
}