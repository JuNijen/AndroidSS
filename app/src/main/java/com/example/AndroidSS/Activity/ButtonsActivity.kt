package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.net.Uri

import com.example.AndroidSS.Func.*
import com.example.AndroidSS.R


//20190916 제작
//20190917 수정 (CALL, TTS)
//20190919 수정 (GPS, RECORD)

class ButtonsActivity : AppCompatActivity()
{
    private lateinit var ttsFunc: TTSFunc
    private lateinit var audioRecordFunc : AudioRecordFunc

    private lateinit var startRecordBtn: Button
    private lateinit var stopRecordBtn: Button
    private lateinit var startPlayBtn: Button
    private lateinit var stopPlayBtn: Button


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_main)

        initActivity()
        setButtons()
    }

    override fun onPause()
    {
        ttsFunc.callStopTTS()
        super.onPause()
    }


    // private fun ---------------------------------------------------------------------------------

    private fun initActivity()
    {
        ttsFunc = TTSFunc()
        ttsFunc.callInitFunc(this)

        audioRecordFunc = AudioRecordFunc()
    }

    private fun setButtons()
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

        setRecordButtons()
    }

    private fun setRecordButtons()
    {
        startRecordBtn = findViewById(R.id.btnRecord)
        stopRecordBtn = findViewById(R.id.btnStop)
        startPlayBtn = findViewById(R.id.btnPlay)
        stopPlayBtn = findViewById(R.id.btnStopPlay)

        //초기 버튼 설정
        startRecordBtn.isEnabled = true
        stopRecordBtn.isEnabled = false
        startPlayBtn.isEnabled = false
        stopPlayBtn.isEnabled = false

        //시작 버튼
        startRecordBtn.setOnClickListener{
            startRecordBtn.isEnabled = false
            stopRecordBtn.isEnabled = true
            startPlayBtn.isEnabled = false
            stopPlayBtn.isEnabled = false

            audioRecordFunc.callStartBtnOnClick(this)
        }

        //정지 버튼
        stopRecordBtn.setOnClickListener{
            startRecordBtn.isEnabled = true
            stopRecordBtn.isEnabled = false
            startPlayBtn.isEnabled = true
            stopPlayBtn.isEnabled = true

            audioRecordFunc.callStopBtnOnClick()
            Toast.makeText(this, R.string.TEXT_AUDIO_RECORD_STOPPED, Toast.LENGTH_SHORT).show()
        }

        //재생 시작 버튼
        startPlayBtn.setOnClickListener{
            startRecordBtn.isEnabled = true
            stopRecordBtn.isEnabled = false
            startPlayBtn.isEnabled = false
            stopPlayBtn.isEnabled = true

            audioRecordFunc.callPlayBtnOnClick()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STARTED,Toast.LENGTH_SHORT).show()
        }

        //재생 정지 버튼
        stopPlayBtn.setOnClickListener{

            startRecordBtn.isEnabled = true
            stopRecordBtn.isEnabled = false
            startPlayBtn.isEnabled = true
            stopPlayBtn.isEnabled = false

            audioRecordFunc.callStopPlayBtnOnClick()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STOPPED, Toast.LENGTH_SHORT).show()
        }
    }

    private fun callBtnOnClick()
    {
        val intent = Intent(Intent.ACTION_CALL)

        //PermissionFunc 를 생성하고 Check 한다.
        //Check 에는 Alert > Request 기능이 포함되어있다.
        if (PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_CALL_PHONE))
        {
            //TODO::인텐트 저친구 뭐가문젠지 잘 모르겠음. 아무튼 해결해야함
            intent.data = Uri.parse("tel:${getString(R.string.TEXT_CALL_NUM)}")
            startActivity(intent)
        }
    }

    private fun gpsBtnBtnOnClick()
    {
        var strCurrentPosition = getString(R.string.TEXT_GPS_CURRENT_POSITION)
        var strAdress = GPSFunc().callGetKoreanAdress(this)

        findViewById<TextView>(R.id.textView1).text = strCurrentPosition + strAdress
    }

    private fun voiceBtnBtnOnClick()
    {

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
        ttsFunc.callPlayTTS(getString(R.string.TEXT_PERMISSION_NOTICE, getString(R.string.TEXT_EMPTY)))
    }
}
