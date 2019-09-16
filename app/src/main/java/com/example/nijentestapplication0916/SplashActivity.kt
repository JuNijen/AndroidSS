package com.example.nijentestapplication0916

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000);

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}