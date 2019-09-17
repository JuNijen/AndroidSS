package com.example.nijentestapplication0916

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//하단부터 추가된 파일.
import android.widget.Button
import android.content.Intent
import android.widget.ImageButton
import android.net.Uri

import android.os.Build
import android.speech.tts.TextToSpeech
import java.util.*


class TestAppButtons : AppCompatActivity()
{

    lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_main)

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener
        {
            status -> if (status != TextToSpeech.ERROR)
            {
                //if there is no error then set language
                mTTS.language = Locale.KOREA
                //mTTS.setLanguage(Locale.getDefault());
            }
        })

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

        PlayTTS("adf");
        /*
        {
            //참고한 자료 ::
            //https://stackoverflow.com/questions/18129712/speak-failed-not-bound-to-tts-engine
        }
        */
    }

    private fun returnHomeBtnOnClick()
    {
        // 메인으로 화면 이동
        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("인텐트 키값","전달할 값")
        startActivity(intent)

        finish()
    }


    private fun PlayTTS(toSpeak: String) {
        //내용이 있어야만 작성 가능.
        if (toSpeak.isNullOrBlank())
        {
            //안내 등 무언가의 처리를 해 주면 좋을듯.
        }
        else
        {
            //롤리팝 이상일 경우
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                mTTS!!.speak("Hello 안녕하세요 가나다 텍스트 입니다.", TextToSpeech.QUEUE_FLUSH, null, "")
            }
            //롤리팝 미만일경우
            else
            {
                @Suppress("DEPRECATION")
                mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    //중지 버튼을 누르면 TTS 중단.
    private fun StopTTS()
    {
        if (mTTS.isSpeaking)
        {
            //동작중이라면, 중단.
            mTTS.stop()
            //mTTS.shutdown()
        }
        else
        {

        }
    }
}
