package com.example.AndroidSS.Service

import android.content.IntentFilter
import android.content.Intent
import android.app.Service
import android.os.IBinder
import android.os.Binder


//20190923 제작
//참고자료 ::
//https://stackoverflow.com/questions/33013400/handle-event-power-button-press-if-activity-is-closed
class NoticePowerOffBtn : Service()
{

    override fun onBind(intent: Intent): IBinder?
    {
        return null
    }

    override fun onCreate()
    {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        val mReceiver = ScreenReceiver()
        registerReceiver(mReceiver, filter)
        return super.onStartCommand(intent, flags, startId)
    }

    //TODO :: 이친구의 존재이유에 대한 고찰이 필요.
    inner class LocalBinder : Binder()
    {
        internal val service: NoticePowerOffBtn
            get() = this@NoticePowerOffBtn
    }
}