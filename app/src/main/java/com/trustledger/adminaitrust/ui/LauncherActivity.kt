package com.trustledger.adminaitrust.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.trustledger.adminaitrust.R
import com.trustledger.adminaitrust.databinding.ActivityLauncherBinding

class LauncherActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE)
        val stationId = sharedPreferences.getString("adminId", null)

        binding.next.setOnClickListener {
            if(stationId == null){
                startActivity(Intent(this , LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this , MainActivity::class.java))
                finish()
            }
        }
    }
}