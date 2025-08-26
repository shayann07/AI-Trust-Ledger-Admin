package com.trustledger.adminaitrust.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.trustledger.adminaitrust.Dao.PlanDao
import com.trustledger.adminaitrust.models.PlanModel
import com.trustledger.adminaitrust.models.TeamSettings
import com.trustledger.adminaitrust.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlanRepository(private val planDao: PlanDao,private val context: Context) {

    val allPlans = planDao.getAllPlans()
    private val firebaseHelper = FirebaseHelper(context)
    private val firestore = FirebaseFirestore.getInstance()

    // Pass context to check network availability
    suspend fun insert(plan: PlanModel, context: Context) {
        planDao.insertPlan(plan)

        if (NetworkUtils.isNetworkAvailable(context)) {
            firebaseHelper.savePlanToFirebase(plan)
        }
    }

    // Pass context to check network availability
    suspend fun updatePlan(plan: PlanModel) {
        firebaseHelper.updatePlanInFirebase(plan)

//        if (NetworkUtils.isNetworkAvailable(context)) {
//            firebaseHelper.updatePlanInFirebase(plan)
//        }
    }
    fun updateTeamSetting(teamSettings: TeamSettings) {
        firebaseHelper.updateTeamSettingInFirebase(teamSettings)
    }

    fun fetchPlansFromFirebase() : LiveData<List<PlanModel>> {
        val plans = firebaseHelper.fetchPlansFromFirebase()
//        updateRoomDatabase(plans as List<PlanModel>)
        return plans
    }


    suspend fun deleteAll() {
        planDao.deleteAllPlans()
    }

    private suspend fun updateRoomDatabase(plans: List<PlanModel>) {
        planDao.insertPlans(plans) // Make sure insertPlans() exists in PlanDao
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    fun fetchTeamSettingsFromFirebase() {
        val teamSettings: MutableList<TeamSettings> = ArrayList()
        firestore.collection("teamSettings").get()
            .addOnSuccessListener { querySnapshot ->
                val teamSettings = querySnapshot.documents.mapNotNull {
                    it.toObject(TeamSettings::class.java)
                }
                saveTeamSettingsToRoom(teamSettings)

            }
            .addOnFailureListener { e ->
                Log.e("FirebaseHelper", "Error fetching team settings", e)
            }


    }

    private fun saveTeamSettingsToRoom(teamSettings: List<TeamSettings>) {
        CoroutineScope(Dispatchers.IO).launch {
            planDao.deleteAllTeamSettings()
            planDao.insertTeamSettings(teamSettings)
        }
    }


    fun fetchTeamSettingsFromRoom(): LiveData<List<TeamSettings>> {
        return planDao.getTeamSettings()
    }
}
