package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.content.Intent
import android.widget.ImageButton
import android.net.Uri
import android.provider.MediaStore
import android.widget.TextView
import com.example.AndroidSS.Func.*
import com.example.AndroidSS.R


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

        SetButtons()
    }

    override fun onPause()
    {
        ttsFunc.CallStopTTS()
        super.onPause()
    }

    private fun SetButtons()
    {
        findViewById<ImageButton>(R.id.callBtn).setOnClickListener {
            callBtnOnClick()
        }

        findViewById<ImageButton>(R.id.gpsBtn).setOnClickListener {
            gpsBtnBtnOnClick()
        }

        findViewById<ImageButton>(R.id.voiceBtn).setOnClickListener {
            voiceBtnBtnOnClick()
        }

        findViewById<ImageButton>(R.id.speakerBtn).setOnClickListener {
            speakerBtnOnClick()
        }

        findViewById<Button>(R.id.returnHomeBtn).setOnClickListener {
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

    private fun gpsBtnBtnOnClick()
    {
        findViewById<TextView>(R.id.textView1).text = GPSFunc().callGetAdress(this)
    }

    private fun voiceBtnBtnOnClick()
    {
        val intent = Intent(this, AudioRecordFunc::class.java)
        startActivity(intent)
    }

    private fun returnHomeBtnOnClick()
    {
        // 메인으로 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    private fun speakerBtnOnClick()
    {
        //TODO:: 이 부분은 임시입니다.
        ttsFunc.CallPlayTTS(getString(R.string.TEXT_PERMISSION_NOTICE, getString(R.string.TEXT_EMPTY)))
    }
}
