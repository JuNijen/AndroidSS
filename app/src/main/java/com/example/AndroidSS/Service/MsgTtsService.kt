package com.example.AndroidSS.Service

import android.speech.tts.TextToSpeech
import android.content.Intent
import android.app.Service
import android.os.IBinder
import android.util.Log
import java.util.*

import com.example.AndroidSS.Func.ContactFunc
import com.example.AndroidSS.Func.TTSFunc
import com.example.AndroidSS.R


//20190925 제작
//참고자료::
//https://stackoverflow.com/questions/5219568/running-android-tts-in-a-service
class MsgTtsService : Service(), TextToSpeech.OnInitListener
{
    private lateinit var ttsFunc: TTSFunc
    private var mTTS: TextToSpeech? = null
    private var mIntent: Intent? = null
    private val TAG = "@@@MsgTtsService"
    private var isInited = false
    private var sleepDelay: Long = 1000
    private var voiceSpeed = 0.7f

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
        //20191001 수정
        //사유 : 이전 메세지가 읽는 중이면 버그가 발생함.
        if (mIntent == null)
        {
            mIntent = intent

            if (isInited)
            {
                setTTS()
            }
        }
        else
        {
            mIntent = intent
            setTTS()
        }

        super.onStart(intent, startId)
    }

    override fun onInit(status: Int)
    {
        //20191001 수정
        //사유 : 이전 메세지가 읽는 중이면 버그가 발생함.
        isInited = true

        setTTS()

        Log.v(TAG, "oninit")
    }

    // public fun ----------------------------------------------------------------------------------

    //TODO::흠 너무 비효율적인가?
    fun callPlayTTS(toSpeak: String, bAddQueue: Boolean = false)
    {
        playTTS(toSpeak, bAddQueue)
    }

    // private fun ---------------------------------------------------------------------------------

    //TODO::흠 너무 비효율적인가?
    private fun playTTS(toSpeak: String, bAddQueue: Boolean = false)
    {
        ttsFunc.callPlayTTS(toSpeak, bAddQueue)
    }

    private fun setInit()
    {
        mTTS = TextToSpeech(this, this)
        Thread.sleep(sleepDelay)
        mTTS!!.language = Locale.getDefault()

        //mTTS!!.setPitch(0.7f)
        mTTS!!.setSpeechRate(voiceSpeed)

        ttsFunc = TTSFunc()
        ttsFunc.callInitFunc(mTTS!!)
    }

    private fun setTTS()
    {
        val strList = mIntent?.getStringArrayListExtra("messageData")
        val maxNum = strList!!.size
        val name = checkContact()

        if (name.isEmpty())
        {
            for (repeatNum in 0 until maxNum)
            {
                //SMS일 경우
                if (maxNum == 1)
                {
                    //QUEUE 추가 없이 FULSH
                    callPlayTTS(strList[repeatNum])
                }
                else if (maxNum >= 2)
                {
                    //QUEUE 첫번째는 FLUSH로 실행 해 주어야 함.
                    if (repeatNum == 0)
                    {
                        callPlayTTS(strList[repeatNum])
                    }
                    else
                    {
                        callPlayTTS(strList[repeatNum], true)
                    }
                }
                Thread.sleep(sleepDelay)
            }
        }
        else
        {
            callPlayTTS(name)
            Thread.sleep(sleepDelay)

            for (repeatNum in 0 until maxNum)
            {
                callPlayTTS(strList[repeatNum], true)
                Thread.sleep(sleepDelay)
            }
        }

        mIntent = null
    }

    private fun checkContact(): String
    {
        val phoneNumber = mIntent?.getStringExtra("senderNumber")
        var name = ""

        if (!phoneNumber.isNullOrEmpty())
        {
            name = ContactFunc().callGetContactName(applicationContext, phoneNumber).toString()

            if(name.isNotEmpty())
            {
                if (name != "null")
                {
                    name = applicationContext.getString(R.string.TEXT_TTS_INFO_OF_SENDER, name)
                }
                else
                {
                    name = applicationContext.getString(R.string.TEXT_TTS_INFO_OF_SENDER, splitPhoneNumber(phoneNumber))
                }
            }
        }
        return name
    }

    //20191001제작
    //참고자료 ::
    //https://stackoverflow.com/questions/53961690/phone-number-auto-formatting-in-android
    private fun splitPhoneNumber(phoneNumber : String) : String
    {
        val textLength = phoneNumber.length
        var resultText = phoneNumber

        if (resultText.endsWith("-") || resultText.endsWith(" "))
        {
            return resultText
        }

        //TODO::상수를 코드에 갖다 박는건 좀 그렇지 않나?

        //000-0000 형태일 경우
        if (textLength == 7)
        {
            if (!resultText.contains("-"))
            {
                resultText = StringBuilder(resultText).insert(resultText.length - 4, "-").toString()
            }
        }

        //0000-0000 형태일 경우
        if (textLength == 8)
        {
            if (!resultText.contains("-"))
            {
                resultText = StringBuilder(resultText).insert(resultText.length - 4, "-").toString()
            }
        }

        //00-000-0000 형태일 경우
        if (textLength == 9)
        {
            if (!resultText.contains("-"))
            {
                resultText = StringBuilder(resultText).insert(resultText.length - 7, "-").toString()
                resultText = StringBuilder(resultText).insert(resultText.length - 4, "-").toString()
            }
        }

        //00-0000-0000 형태일 경우
        //000-000-0000 형태일 경우
        if (textLength == 10)
        {
            if (!resultText.contains("-"))
            {
                //resultText의 2번째 글자가 0일 경우
                //지역은 서울이다.
                //00-0000-0000의 형태
                if(resultText[1] == '2')
                {
                    resultText = StringBuilder(resultText).insert(resultText.length - 8, "-").toString()
                    resultText = StringBuilder(resultText).insert(resultText.length - 4, "-").toString()
                }
                else
                {
                    resultText = StringBuilder(resultText).insert(resultText.length - 7, "-").toString()
                    resultText = StringBuilder(resultText).insert(resultText.length - 4, "-").toString()
                }
            }
        }

        //000-0000-0000 형태일 경우
        if (textLength == 11)
        {
            if (!resultText.contains("-"))
            {
                resultText = StringBuilder(resultText).insert(resultText.length - 8, "-").toString()
                resultText = StringBuilder(resultText).insert(resultText.length - 4, "-").toString()
            }
        }

        return resultText
    }
}