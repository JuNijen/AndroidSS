package com.example.AndroidSS.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.media.audiofx.Visualizer
import android.net.Uri

import com.example.AndroidSS.Func.*
import com.example.AndroidSS.R
import android.view.View
import com.example.AndroidSS.Service.PowerOffService
import com.gauravk.audiovisualizer.visualizer.BarVisualizer


//20190916 제작
//20190917 수정 (CALL, TTS)
//20190919 수정 (GPS, RECORD)

class ButtonsActivity : AppCompatActivity()
{
    private lateinit var ttsFunc: TTSFunc
    private lateinit var audioRecordFunc : AudioRecordFunc

    private lateinit var mStartRecordBtn: Button
    private lateinit var mStopRecordBtn: Button
    private lateinit var mStartPlayBtn: Button
    private lateinit var mStopPlayBtn: Button

    private var mVisualizer: BarVisualizer? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_main)

        initActivity()
        setVisualizer()
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
        mStartRecordBtn = findViewById(R.id.btnRecord)
        mStopRecordBtn = findViewById(R.id.btnStop)
        mStartPlayBtn = findViewById(R.id.btnPlay)
        mStopPlayBtn = findViewById(R.id.btnStopPlay)

        //초기 버튼 설정
        mStartRecordBtn.isEnabled = true
        mStopRecordBtn.isEnabled = false
        mStartPlayBtn.isEnabled = false
        mStopPlayBtn.isEnabled = false

        //시작 버튼
        mStartRecordBtn.setOnClickListener{
            mStartRecordBtn.isEnabled = false
            mStopRecordBtn.isEnabled = true
            mStartPlayBtn.isEnabled = false
            mStopPlayBtn.isEnabled = false

            audioRecordFunc.callStartBtnOnClick(this)
        }

        //정지 버튼
        mStopRecordBtn.setOnClickListener{
            mStartRecordBtn.isEnabled = true
            mStopRecordBtn.isEnabled = false
            mStartPlayBtn.isEnabled = true
            mStopPlayBtn.isEnabled = true

            audioRecordFunc.callStopBtnOnClick()
            Toast.makeText(this, R.string.TEXT_AUDIO_RECORD_STOPPED, Toast.LENGTH_SHORT).show()
        }

        //재생 시작 버튼
        mStartPlayBtn.setOnClickListener{
            mStartRecordBtn.isEnabled = true
            mStopRecordBtn.isEnabled = false
            mStartPlayBtn.isEnabled = false
            mStopPlayBtn.isEnabled = true

            audioRecordFunc.callPlayBtnOnClick()
            startVisualizer()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STARTED,Toast.LENGTH_SHORT).show()
        }

        //재생 정지 버튼
        mStopPlayBtn.setOnClickListener{

            mStartRecordBtn.isEnabled = true
            mStopRecordBtn.isEnabled = false
            mStartPlayBtn.isEnabled = true
            mStopPlayBtn.isEnabled = false

            audioRecordFunc.callStopPlayBtnOnClick()
            stopVisualizer()
            Toast.makeText(this,R.string.TEXT_AUDIO_RECORD_LISTENING_STOPPED, Toast.LENGTH_SHORT).show()
        }
    }

    //20190923 추가 - audio wave visualizer 추가를 위함.
    //참고자료 ::
    //https://androidexample365.com/a-light-weight-and-easy-to-use-audio-visualizer-for-android/
    //20190923 수정 - null check, add release
    //https://github.com/gauravk95/audio-visualizer-android/blob/c0c0bb29cc12c4b09eb573b12c517256b629e1b7/app/src/main/java/com/gauravk/audiovisualizersample/ui/CircleLineActivity.java
    private fun setVisualizer()
    {
        mVisualizer = findViewById<View>(R.id.barVisualizer) as BarVisualizer
    }

    private fun startVisualizer()
    {
        //get the AudioSessionId from your MediaPlayer and pass it to the visualizer
        val audioSessionId = audioRecordFunc.callGetAudioSessionID()

        if (audioSessionId != -1)
        {
            if(mVisualizer != null)
            {
                mVisualizer?.show()
                mVisualizer?.setAudioSessionId(audioSessionId)
            }
        }
    }

    private fun stopVisualizer()
    {
        //TODO: check for completion of audio eg. using MediaPlayer.OnCompletionListener()
        if (mVisualizer != null)
        {
            mVisualizer?.clearAnimation()
            mVisualizer?.release()
            mVisualizer?.hide()
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
        //TODO::여기에 있어야 할까.
        var bInternet = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_INTERNET)
        var bReadSms = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_READ_SMS)
        var bReceiveSms = PermissionFunc().callCheckPermission(this, MY_PERMISSION.E_RECEIVE_SMS)

        if(!bInternet)
            PermissionFunc().callRequestPermission(this, MY_PERMISSION.E_INTERNET)
        if(!bReadSms)
            PermissionFunc().callRequestPermission(this, MY_PERMISSION.E_READ_SMS)
        if(!bReceiveSms)
            PermissionFunc().callRequestPermission(this, MY_PERMISSION.E_RECEIVE_SMS)

        //20190924 PowerOffSeivice (가제)의 적용을 위하여 추가됨.
        //TODO::꼭 여기에 넣어줬어야했는가? 다른데 어디에 넣어야 할 지는 모르겠음.
        this.startService(Intent(this, PowerOffService::class.java))
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
