package org.ileewe.theseedjcli.viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ileewe.theseedjcli.repository.MinistriesRepository
import org.ileewe.theseedjcli.viewmodel.MinistriesViewModel

class MinistriesViewModelProviderFactory( private val repository: MinistriesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MinistriesViewModel::class.java)) {
            return MinistriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}