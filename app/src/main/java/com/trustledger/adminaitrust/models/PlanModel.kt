package com.trustledger.adminaitrust.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.io.Serializable

@Entity(tableName = "plans")

data class PlanModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val docId: String = "",
    val type: String = "",
    val planName: String= "",
    val minAmount: Int = 0,
    val planDays: Int = 0,
    val dailyPercentage: Float = 0f,
    val directProfit: Float = 0f,
    val timestamp: Timestamp = Timestamp.now()
) : Serializable
