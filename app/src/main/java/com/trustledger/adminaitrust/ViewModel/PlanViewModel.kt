package com.trustledger.adminaitrust.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.trustledger.adminaitrust.models.PlanModel
import com.trustledger.adminaitrust.models.TeamSettings
import com.trustledger.adminaitrust.repository.PlanRepository
import kotlinx.coroutines.launch

class PlanViewModel(application: Application, private val repository: PlanRepository) : AndroidViewModel(application) {

    val allPlans: LiveData<List<PlanModel>> = repository.allPlans

    init {
        repository.fetchTeamSettingsFromFirebase()
    }

    fun insertPlan(plan: PlanModel, context: Context) = viewModelScope.launch {
        repository.insert(plan, context)
    }

    fun updatePlan(plan: PlanModel) = viewModelScope.launch {
        repository.updatePlan(plan)
    }

    fun clearPlans() = viewModelScope.launch {
        repository.deleteAll()
    }
    fun refreshData() {
        viewModelScope.launch {
            // Trigger the refresh process in the repository
            repository.fetchPlansFromFirebase()

            //
        }
    }
    fun fetchPlansFromFirebase() : LiveData<List<PlanModel>>  {
        return repository.fetchPlansFromFirebase()
    }

    ///////////////////////////////////////////////////////////////////

    fun fetchTeamSettingsFromRoom(): LiveData<List<TeamSettings>> {
        return repository.fetchTeamSettingsFromRoom()
    }
    fun updateTeamSetting(teamSettings: TeamSettings) = viewModelScope.launch {
        repository.updateTeamSetting(teamSettings)
    }
}
