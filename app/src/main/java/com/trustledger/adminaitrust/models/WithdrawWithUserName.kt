package com.trustledger.adminaitrust.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.trustledger.adminaitrust.utils.WithdrawModelConverter

@Entity(tableName = "withdrawals")
data class WithdrawWithUserName(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @TypeConverters(WithdrawModelConverter::class) val withdraw: WithdrawModel,
    val userName: String = "",
    val lastName: String = ""
)
