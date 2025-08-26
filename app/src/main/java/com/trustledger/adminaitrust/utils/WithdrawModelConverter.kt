package com.trustledger.adminaitrust.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trustledger.adminaitrust.models.WithdrawModel

class WithdrawModelConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromWithdrawModel(withdraw: WithdrawModel): String {
        return gson.toJson(withdraw)
    }

    @TypeConverter
    fun toWithdrawModel(withdrawJson: String): WithdrawModel {
        val type = object : TypeToken<WithdrawModel>() {}.type
        return gson.fromJson(withdrawJson, type)
    }
}
