package com.example.currencytracking.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencytracking.R
import com.example.currencytracking.data.model.RateCurrency

class RateCurrencyAdapter(
    val onFavoriteClick: (RateCurrency) -> Unit
) : RecyclerView.Adapter<RateCurrencyAdapter.RateCurrencyHolder>() {

    var list: List<RateCurrency> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateCurrencyHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rate, parent, false)
        return RateCurrencyHolder(view)
    }

    override fun onBindViewHolder(holder: RateCurrencyHolder, position: Int) {
        holder.setUser(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class RateCurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtRate: TextView
        private val txtCurrency: TextView
        private val imgFavorite: View

        init {
            txtRate = itemView.findViewById(R.id.txtRate)
            txtCurrency = itemView.findViewById(R.id.txtCurrency)
            imgFavorite = itemView.findViewById(R.id.imgFavorite)
        }

        fun setUser(rateCurrency: RateCurrency) {
            txtRate.text = rateCurrency.rate.toString()
            txtCurrency.text = rateCurrency.currency
            imgFavorite.setOnClickListener { onFavoriteClick(rateCurrency) }
        }
    }
}