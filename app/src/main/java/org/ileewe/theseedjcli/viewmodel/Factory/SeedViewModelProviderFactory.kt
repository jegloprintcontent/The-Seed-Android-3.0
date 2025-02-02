package org.ileewe.theseedjcli.viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ileewe.theseedjcli.repository.SeedRepository
import org.ileewe.theseedjcli.viewmodel.SeedViewModel

class SeedViewModelProviderFactory(private val repository: SeedRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(SeedViewModel::class.java)) {
            return SeedViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown viewModel class")
    }

}