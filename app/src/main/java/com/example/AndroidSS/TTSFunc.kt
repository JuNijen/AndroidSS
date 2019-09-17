package com.example.AndroidSS

//하단부터 추가된 파일.
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.os.Build

//20190917 제작
//제작에 참고한 자료 ::
//https://devofandroid.blogspot.com/2018/10/text-to-speech-android-studio-kotlin.html
//수정에 참고한 자료 ::
//https://gist.github.com/leesc22/5f13cc049e84610fe147f21ab7e4b814

class TTSFunc
{
    lateinit var mTTS: TextToSpeech

    fun CallInitFunc(arg_app_compact_activity : AppCompatActivity)
    {
        InitFunc(arg_app_compact_activity)
    }

    fun CallPlayTTS(toSpeak : String)
    {
        PlayTTS(toSpeak)
    }

    fun CallStopTTS()
    {
        StopTTS()
    }

    private fun InitFunc(arg_app_compact_activity : AppCompatActivity)
    {
        mTTS = TextToSpeech(arg_app_compact_activity, TextToSpeech.OnInitListener
        {
                status -> if (status != TextToSpeech.ERROR)
        {
            //if there is no error then set language
            mTTS.language = Locale.KOREA
            //mTTS.setLanguage(Locale.getDefault());
        }
        })
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
                mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
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

    //TODO::
    //TTSFunc를 사용중인 곳에 추가하여야 할 것.
    //override fun onPause()
    //{
    //    if (mTTS.isSpeaking)
    //    {
    //        //if speaking then stop
    //        mTTS.stop()
    //        //mTTS.shutdown()
    //    }
    //    super.onPause()
    //}
}