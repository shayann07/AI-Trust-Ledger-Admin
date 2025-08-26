package com.trustledger.adminaitrust.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trustledger.adminaitrust.models.AccountModel
import com.trustledger.adminaitrust.models.TeamSettings
import com.trustledger.adminaitrust.models.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<UserModel>)

    @Query("SELECT * FROM user_table")
    fun getAllUsersFlow(): Flow<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAccounts(accounts: List<AccountModel>)

    @Query("SELECT * FROM accounts_table")
    fun getAllAccountsFlow(): Flow<List<AccountModel>>

}