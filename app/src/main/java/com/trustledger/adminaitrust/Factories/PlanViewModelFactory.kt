package com.trustledger.adminaitrust.Factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.trustledger.adminaitrust.ViewModel.PlanViewModel
import com.trustledger.adminaitrust.repository.PlanRepository

class PlanViewModelFactory(
    private val application: Application,
    private val planRepository: PlanRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")  // Suppress the unchecked cast warning
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
            return PlanViewModel(application, planRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
