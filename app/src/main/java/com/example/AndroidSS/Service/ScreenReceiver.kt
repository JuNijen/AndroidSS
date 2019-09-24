package com.example.AndroidSS.Service

import android.content.BroadcastReceiver
import android.media.MediaPlayer
import android.content.Context
import android.content.Intent
import java.io.IOException
import android.util.Log
import com.example.AndroidSS.R


//20190923 제작
//참고자료 ::
//https://stackoverflow.com/questions/33013400/handle-event-power-button-press-if-activity-is-closed
class ScreenReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        //onReceive 로 들어왔음을 알리는 로그.
        Log.e("@@@", "@@@onReceive")

        if (intent.action == Intent.ACTION_SCREEN_OFF)
        {
            wasScreenOn = false
            Log.e("@@@", "@@@wasScreenOn$wasScreenOn")

            //일단은 꺼졌을 때 소리를 들려주는 걸로 해 보자.
            soundTest(context, R.raw.sasara_thunder)
        }
        else if (intent.action == Intent.ACTION_SCREEN_ON)
        {
            wasScreenOn = true
            Log.e("@@@", "@@@wasScreenOn$wasScreenOn")

            //일단은 켜졌을 때도 소리를 들려주는 걸로 해 보자.
            soundTest(context, R.raw.sasara_twister)
        }
        else if (intent.action == Intent.ACTION_USER_PRESENT)
        {
            Log.e("@@@", "@@@userpresent")
            //do something.
        }
    }

    companion object
    {
        var wasScreenOn = true
    }


    //참고자료 ::
    //https://stackoverflow.com/questions/1283499/setting-data-source-to-an-raw-id-in-mediaplayer
    private fun soundTest(context: Context, soundCode : Int)
    {
        //var soundPlayer= MediaPlayer.create(context, soundCode)

        val assetFileDescriptor = context.resources.openRawResourceFd(soundCode) ?: return
        var soundPlayer = MediaPlayer()

        soundPlayer?.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
        assetFileDescriptor.close()

        try
        {
            soundPlayer?.prepare()
            soundPlayer?.start()
        }
        catch (e: IOException)
        {
            //Log.e("@@@PlayBtn 몬가 문제가 생김 ",mFileName)
        }
    }
}