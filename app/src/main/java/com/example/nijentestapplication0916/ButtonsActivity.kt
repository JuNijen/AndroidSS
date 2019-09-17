package com.example.nijentestapplication0916

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.content.Intent
import android.widget.ImageButton
import android.net.Uri


class TestAppButtons : AppCompatActivity()
{

    lateinit var ttsFunc : TTSFunc

    override fun onCreate(savedInstanceState: Bundle?)
    {
        ttsFunc = TTSFunc()
        ttsFunc.CallInitFunc(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_main)

        SetMain()
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
        val intent = Intent(Intent.ACTION_CALL);

        if (CheckPermission()) {
            //TODO::
            //전화번호가 잘못 전달되는 문제가 발견됨. 수정해야함.
            //인텐트 저친구 뭐가문젠지 모르겠음. 아무튼 해결해야함
            intent.data = Uri.parse("tel:${R.string.TEXT_CALL_NUM}")
            startActivity(intent)
        }
    }

    private fun CheckPermission(): Boolean
    {
        var bReady = false
        var permissionActivity = PermissionActivity()

        if (!permissionActivity.CallCheckPermission(this)) {
            //권한이 없을 경우 요청한다.
            permissionActivity.CallRequestPermission(this)
        } else {
            bReady = true
        }
        return bReady
    }

    private fun alarmBtnOnClick()
    {

    }

    private fun voiceBtnBtnOnClick()
    {
        ttsFunc.CallPlayTTS("asef")
    }

    private fun returnHomeBtnOnClick()
    {
        // 메인으로 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("인텐트 키값","전달할 값")
        startActivity(intent)

        finish()
    }
}
