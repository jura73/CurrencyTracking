package com.example.currencytracking.data.service

import com.example.currencytracking.data.model.LatestModel
import retrofit2.http.GET
import retrofit2.http.Headers

internal interface CurrencyService {

    @Headers("Accept: application/json")
    @GET("api/latest.json")
    suspend fun latest(): LatestModel

}