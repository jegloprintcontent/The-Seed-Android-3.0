package org.ileewe.theseedjcli.viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ileewe.theseedjcli.repository.HomeRepository
import org.ileewe.theseedjcli.viewmodel.HomeViewModel

class HomeViewModelProviderFactory( private val repository: HomeRepository):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository) as T
            }

            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
