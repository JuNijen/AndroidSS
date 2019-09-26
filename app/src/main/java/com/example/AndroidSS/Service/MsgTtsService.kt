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
        Log.v(TAG, "oncreate_service")
        setInit()

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
        setTTS(intent)

        super.onStart(intent, startId)
    }

    override fun onInit(status: Int)
    {
        Log.v(TAG, "oninit")
    }

    // public fun ----------------------------------------------------------------------------------

    //TODO::흠 너무 비효율적인가?
    fun callPlayTTS(toSpeak: String,  bAddQueue : Boolean = false)
    {
        playTTS(toSpeak, bAddQueue)
    }

    // private fun ---------------------------------------------------------------------------------

    //TODO::흠 너무 비효율적인가?
    private fun playTTS(toSpeak: String,  bAddQueue : Boolean = false)
    {
        ttsFunc.callPlayTTS(toSpeak, bAddQueue)
    }

    private fun setInit()
    {
        mTTS = TextToSpeech(this,this)
        Thread.sleep(1000)
        mTTS!!.language = Locale.getDefault()

        //mTTS!!.setPitch(0.7f)
        mTTS!!.setSpeechRate(0.75f)

        ttsFunc = TTSFunc()
        ttsFunc.callInitFunc(mTTS!!)
    }

    private fun setTTS(intent: Intent)
    {
        var strList = intent.getStringArrayListExtra("message")
        var maxNum = strList.size

        for(repeatNum in 0 until maxNum)
        {
            //SMS일 경우
            if(maxNum == 1)
            {
                //QUEUE 추가 없이 FULSH
                callPlayTTS(strList[repeatNum])
            }
            else if(maxNum >= 2)
            {
                //QUEUE 첫번째는 FLUSH로 실행 해 주어야 함.
                if(repeatNum == 0)
                {
                    callPlayTTS(strList[repeatNum])
                }
                else
                {
                    //QUEUE에 추가.
                    callPlayTTS(strList[repeatNum], true)
                }
            }
        }

    }
}