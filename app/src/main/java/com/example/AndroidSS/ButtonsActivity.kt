package com.example.AndroidSS

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.content.Intent
import android.widget.ImageButton
import android.net.Uri


//20190916 제작
//20190917 수정 (CALL, TTS)

class ButtonsActivity : AppCompatActivity()
{

    lateinit var ttsFunc: TTSFunc

    override fun onCreate(savedInstanceState: Bundle?)
    {
        ttsFunc = TTSFunc()
        ttsFunc.CallInitFunc(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_main)

        SetMain()
    }

    override fun onPause()
    {
        ttsFunc.CallStopTTS()
        super.onPause()
    }

    private fun SetMain()
    {
        var callBtn = findViewById<ImageButton>(R.id.callBtn).setOnClickListener {
            callBtnOnClick()
        }

        var alarmBtn = findViewById<ImageButton>(R.id.alarmBtn).setOnClickListener {
            alarmBtnOnClick()
        }

        var voiceBtn = findViewById<ImageButton>(R.id.voiceBtn).setOnClickListener {
            voiceBtnBtnOnClick()
        }

        var returnHomeBtn = findViewById<Button>(R.id.returnHomeBtn).setOnClickListener {
            returnHomeBtnOnClick()
        }
    }

    private fun callBtnOnClick()
    {
        val intent = Intent(Intent.ACTION_CALL)

        //PermissionFunc를 생성하고 Check 한다.
        //Check에는 Alart > Request 기능이 포함되어있다.
        if (PermissionFunc().CallCheckPermission(this, MY_PERMISSION.E_CALL_PHONE))
        {
            //TODO::인텐트 저친구 뭐가문젠지 잘 모르겠음. 아무튼 해결해야함
            intent.data = Uri.parse("tel:${getString(R.string.TEXT_CALL_NUM)}")
            startActivity(intent)
        }
    }

    private fun alarmBtnOnClick()
    {
        val intent = Intent(this, GPSFunc::class.java)
        startActivity(intent)
    }

    private fun voiceBtnBtnOnClick()
    {
        //TODO:: 이 부분은 임시입니다.
        ttsFunc.CallPlayTTS(getString(R.string.TEXT_PERMISSION_NOTICE))
    }

    private fun returnHomeBtnOnClick()
    {
        // 메인으로 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}
