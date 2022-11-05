package com.example.currencytracking.di

import com.example.currencytracking.data.repositories.CurrencyRepository
import com.example.currencytracking.data.repositories.ICurrencyRepository
import com.example.currencytracking.data.service.CurrencyService
import com.example.currencytracking.data.service.RetrofitHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
internal interface HomeViewModel {

    @Binds
    fun bindCurrencyRepository(currencyRepository: CurrencyRepository): ICurrencyRepository

}

@Module
@InstallIn(SingletonComponent::class)
internal object CurrencyModule {

    @Singleton
    @Provides
    fun provideCurrencyService(): CurrencyService {
        return RetrofitHelper.getRetrofit().create(CurrencyService::class.java)
    }
}