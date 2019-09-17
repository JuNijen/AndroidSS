package com.example.nijentestapplication0916

//하단부터 추가된 파일.
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.util.Log
import android.os.Build


//제작에 참고한 자료 ::
//https://devofandroid.blogspot.com/2018/10/text-to-speech-android-studio-kotlin.html
//수정에 참고한 자료 ::
//https://gist.github.com/leesc22/5f13cc049e84610fe147f21ab7e4b814

class TTSFunc
{
    lateinit var mTTS: TextToSpeech

    fun CallCheckTTSStatus(arg_app_compact_activity : AppCompatActivity) : Boolean
    {
        return CheckTTSStatus(arg_app_compact_activity)
    }

    fun CallPlayTTS(toSpeak : String)
    {
        PlayTTS(toSpeak)
    }

    fun CallStopTTS()
    {
        StopTTS()
    }


    private fun CheckTTSStatus(arg_app_compact_activity : AppCompatActivity) : Boolean
    {
        var bReady = false
        mTTS = TextToSpeech(arg_app_compact_activity, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR)
            {
                //if there is no error then set language
                mTTS.language = Locale.KOREA
                bReady = true
            }
            else
            {
                Log.e("TTS :: ", "TTSFunc.kt" + R.string.ERR_ERR_FROM_CODE);
            }
        })
        return bReady
    }

    private fun PlayTTS(toSpeak : String)
    {
        //내용이 있어야만 작성 가능.
        //if (toSpeak.isNullOrBlank()) toSpeak = "Please enter a message"
        if (toSpeak != "")
        {
            //mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            //mTTS!!.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "")

            //롤리팝 이상일 경우
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                mTTS!!.speak("안녕하세요 가나다 텍스트 입니다.", TextToSpeech.QUEUE_FLUSH, null, "")
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
    //TTS를 사용중인 곳에 추가하여야 할 것.
    //fun CheckYourCode()
    //{
    //    override fun onPause()
    //    {
    //        if (mTTS.isSpeaking)
    //        {
    //            //if speaking then stop
    //            mTTS.stop()
    //            //mTTS.shutdown()
    //        }
    //        super.onPause()
    //    }
    //}
}