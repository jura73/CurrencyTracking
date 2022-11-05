package com.example.currencytracking.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyEntity(@PrimaryKey val currency : String)