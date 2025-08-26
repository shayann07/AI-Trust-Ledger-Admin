package com.trustledger.adminaitrust.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trustledger.adminaitrust.Dao.PlanDao
import com.trustledger.adminaitrust.Dao.UserDao
import com.trustledger.adminaitrust.Dao.WithdrawDao
import com.trustledger.adminaitrust.models.AccountModel
import com.trustledger.adminaitrust.models.PlanModel
import com.trustledger.adminaitrust.models.TeamSettings
import com.trustledger.adminaitrust.models.UserModel
import com.trustledger.adminaitrust.models.WithdrawModel
import com.trustledger.adminaitrust.models.WithdrawWithUserName
import com.trustledger.adminaitrust.utils.Converters
import com.trustledger.adminaitrust.utils.TimestampConverter
import com.trustledger.adminaitrust.utils.WithdrawModelConverter


@Database(entities = [UserModel::class, AccountModel::class, PlanModel::class, WithdrawModel::class, WithdrawWithUserName::class, TeamSettings::class], version = 2, exportSchema = false)
@TypeConverters(TimestampConverter::class,WithdrawModelConverter::class,Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun userDao(): UserDao
    abstract fun withdrawDao(): WithdrawDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Define the getInstance method
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "investment_database"
                )
                    .fallbackToDestructiveMigration()  // Prevents crashes due to schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
