package com.example.AndroidSS.Func

//하단부터 추가된 파일.
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import java.util.*

//20190917 제작
//제작에 참고한 자료 ::
//https://devofandroid.blogspot.com/2018/10/text-to-speech-android-studio-kotlin.html
//수정에 참고한 자료 ::
//https://gist.github.com/leesc22/5f13cc049e84610fe147f21ab7e4b814
//20190925 수정
//참고자료::
//https://stackoverflow.com/questions/5219568/running-android-tts-in-a-service
//20190930 수정 isNullOrBlank > isNullOrEmpty

class TTSFunc
{
    private lateinit var mTTS: TextToSpeech

    // public fun ----------------------------------------------------------------------------------

    fun callInitFunc(textToSpeech: TextToSpeech)
    {
        initFunc(textToSpeech)
    }

    fun callInitFunc(appCompactActivity: AppCompatActivity)
    {
        initFunc(appCompactActivity)
    }

    fun callPlayTTS(toSpeak: String,  bAddQueue : Boolean = false)
    {
        playTTS(toSpeak, bAddQueue)
    }

    fun callStopTTS()
    {
        stopTTS()
    }


    // private fun ---------------------------------------------------------------------------------

    private fun initFunc(textToSpeech: TextToSpeech)
    {
        mTTS = textToSpeech
    }

    private fun initFunc(appCompactActivity: AppCompatActivity)
    {
        mTTS = TextToSpeech(appCompactActivity, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR)
            {
                //if there is no error then set language
                mTTS.language = Locale.getDefault()
            }
        })
    }

    private fun playTTS(toSpeak: String, bAddQueue : Boolean = false)
    {
        //내용이 있어야만 작성 가능.
        if (toSpeak.isNullOrEmpty())
        {
            //안내 등 무언가의 처리를 해 주면 좋을듯.
        }
        else
        {
            //롤리팝 이상일 경우
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                if(bAddQueue)
                {
                    mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_ADD, null, "")
                }
                else
                {
                    mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
                }
            }
            //롤리팝 미만일경우
            else
            {
                if(bAddQueue)
                {
                    @Suppress("DEPRECATION") mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_ADD, null)
                }
                else
                {
                    @Suppress("DEPRECATION") mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }
    }

    //중지 버튼을 누르면 TTS 중단.
    private fun stopTTS()
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

    //TTSFunc를 사용중인 곳에 추가하여야 할 것.
    //override fun onPause()
    //{
    //    ttsFunc.callStopTTS()
    //    super.onPause()
    //}
}