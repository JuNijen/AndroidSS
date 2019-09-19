package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle

//20190916 제작

class SplashActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        Thread.sleep(1000)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}