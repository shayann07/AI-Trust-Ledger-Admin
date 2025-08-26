package com.trustledger.adminaitrust.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.trustledger.adminaitrust.models.WithdrawModel
import com.trustledger.adminaitrust.models.WithdrawWithUserName

import com.trustledger.adminaitrust.repository.WithdrawRepository
import kotlinx.coroutines.launch


class WithdrawViewModel(
    application: Application,
    private val repository: WithdrawRepository
) : AndroidViewModel(application) {

    val localWithdrawals: LiveData<List<WithdrawWithUserName>> = repository.allWithdrawRequests
    private val _localWithdrawals = MutableLiveData<List<WithdrawWithUserName>>()
    fun saveWithdrawLocally(withdraw: WithdrawModel) = viewModelScope.launch {
        repository.insert(withdraw)
    }

    fun clearLocalWithdrawals() = viewModelScope.launch {
        repository.clearAll()
    }

    fun deleteByStatus(status: String) = viewModelScope.launch {
        repository.deleteByStatus(status)
    }

    fun refreshWithdrawsFromFirebase() = viewModelScope.launch {
        repository.refreshDataFromFirebase()
    }
//    fun removeFromLocal(transactionId: String) {
//        _localWithdrawals.value = _localWithdrawals.value?.filterNot { it.withdraw.transactionId == transactionId }
//    }

}
