package com.example.AndroidSS.Service

import android.app.Service
import android.speech.tts.TextToSpeech
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

import com.example.AndroidSS.Func.TTSFunc


//20190925 제작
//참고자료::
//https://stackoverflow.com/questions/5219568/running-android-tts-in-a-service
class MsgTtsService : Service(), TextToSpeech.OnInitListener
{
    private lateinit var ttsFunc: TTSFunc
    private var mTTS: TextToSpeech? = null
    private val TAG = "@@@MsgTtsService"

    override fun onBind(arg0: Intent): IBinder?
    {
        return null
    }

    override fun onCreate()
    {
        mTTS = TextToSpeech(this,this)
        mTTS!!.language = Locale.getDefault()

        ttsFunc = TTSFunc()
        ttsFunc.callInitFunc(mTTS!!)

        Log.v(TAG, "oncreate_service")
        super.onCreate()
    }

    override fun onDestroy()
    {
        // TODO Auto-generated method stub
        if (mTTS != null)
        {
            mTTS!!.stop()
            mTTS!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onStart(intent: Intent, startId: Int)
    {
        Log.v(TAG, "onstart_service")
        callPlayTTS(intent.getStringExtra("message"))

        super.onStart(intent, startId)
    }

    override fun onInit(status: Int)
    {
        Log.v(TAG, "oninit")
    }

    // public fun ----------------------------------------------------------------------------------

    //TODO::흠 너무 비효율적인가?
    fun callPlayTTS(toSpeak: String)
    {
        playTTS(toSpeak)
    }

    // private fun ---------------------------------------------------------------------------------

    //TODO::흠 너무 비효율적인가?
    private fun playTTS(toSpeak: String)
    {
        ttsFunc.callPlayTTS(toSpeak)
    }
}