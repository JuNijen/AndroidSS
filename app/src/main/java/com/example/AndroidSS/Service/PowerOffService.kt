package com.example.AndroidSS.Service

import android.content.IntentFilter
import android.content.Intent
import android.app.Service
import android.os.IBinder
import android.os.Binder

import android.content.BroadcastReceiver


//20190923 제작
//이건 따로 필요없을듯 해 보임. 일단은 동작 확인 될 때 까지 충실하게 하는걸로...
//참고자료 ::
//https://stackoverflow.com/questions/33013400/handle-event-power-button-press-if-activity-is-closed
class PowerOffService : Service()
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

    inner class LocalBinder : Binder()
    {
        internal val service: PowerOffService
            get() = this@PowerOffService
    }
}