package com.trustledger.adminaitrust.repository


import android.content.Context
import androidx.lifecycle.LiveData
import com.trustledger.adminaitrust.Dao.PlanDao
import com.trustledger.adminaitrust.Dao.WithdrawDao
import com.trustledger.adminaitrust.models.WithdrawModel
import com.trustledger.adminaitrust.models.WithdrawWithUserName

class WithdrawRepository(private val withdrawDao: WithdrawDao,private val context: Context) {

    private val firebaseHelper = FirebaseHelper(context)

    val allWithdrawRequests: LiveData<List<WithdrawWithUserName>> = withdrawDao.getAllWithdrawRequests()

    suspend fun insert(withdraw: WithdrawModel) {
        withdrawDao.insertWithdrawRequest(withdraw)
    }

    suspend fun refreshDataFromFirebase() {
        val requestsWithNames = firebaseHelper.fetchWithdrawRequestsWithUserNames()
        clearAll()
        withdrawDao.insertAll(requestsWithNames)
    }

    suspend fun clearAll() {
        withdrawDao.deleteAll()
    }
    suspend fun deleteByStatus(status: String) {
        withdrawDao.deleteByStatus(status)
    }
}
