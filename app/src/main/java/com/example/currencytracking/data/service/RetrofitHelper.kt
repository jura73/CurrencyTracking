package com.example.currencytracking.data.service

import com.example.currencytracking.data.model.RateCurrency
import com.example.currencytracking.data.model.Rates
import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetrofitHelper {

    class RatesAdapter : JsonDeserializer<Rates> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Rates {
            val list: MutableList<RateCurrency> = mutableListOf()
            val jsonObject = json?.asJsonObject
            jsonObject?.keySet()?.forEach {
                list.add(RateCurrency(it, jsonObject.get(it).asDouble))
            }
            return Rates(list)
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/")
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getGson(): Gson = GsonBuilder()
        .registerTypeAdapter(Rates::class.java, RatesAdapter())
        .create()

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(getRequestInterceptor())
            .build()
    }

    private fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original: Request = chain.request()

            val request: Request = original.newBuilder()
                .header("Authorization", "Token cdd582d853d94634a84345d0f388f9f4")
                .build()

            chain.proceed(request)
        }
    }

}