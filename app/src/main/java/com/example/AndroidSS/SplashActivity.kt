package com.example.AndroidSS

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//20190916 제작

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000);

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}