package com.example.currencytracking.ui.favorites

import androidx.fragment.app.viewModels
import com.example.currencytracking.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class FavoritesFragment : HomeFragment() {

    override val homeViewModel: FavoritesViewModel by viewModels()

}