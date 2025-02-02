package org.ileewe.theseedjcli.viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ileewe.theseedjcli.repository.SermonsRepository
import org.ileewe.theseedjcli.viewmodel.SermonsViewModel

class SermonViewModelProviderFactory(private val repository: SermonsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SermonsViewModel::class.java)) {
            return SermonsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown viewModel class")
    }
}