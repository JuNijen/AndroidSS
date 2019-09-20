package com.example.AndroidSS.Func

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.AndroidSS.Activity.SplashActivity


//20190920 제작
//https://kitesoft.tistory.com/79

class StartReceiver : BroadcastReceiver()
{
    //BroadCast를 받앗을때 자동으로 호출되는 콜백 메소드
    //첫번째 파라미터 : Context 컨텍스트
    //두번째 파라미터 : BroadCast의 정보를 가지고 있는 Intent..
    override fun onReceive(context: Context?, intent: Intent?)
    {
        //수신받은 방송(Broadcast)의 Action을 얻어오기
        //메니페스트 파일안에 이 Receiver에게 적용된 필터(Filter)의 Action만 받아오게 되어 있음.
        var intentAction = intent?.action

        //수신된 action값이 시스템의 '부팅 완료'가 맞는지 확인..

        if (intentAction.equals("android.intent.action.BOOT_COMPLETED"))
        {
            //TODO :: SplashActivity로 해야 할 지, MainActivity로 해야할지 조금 의문.
            val intent = Intent(context, SplashActivity::class.java)

            //위에 만들어진 Intent에 의해 실행되는 Activity는
            //액티비티 스택에서 새로운 Task로 Activity를 실행하도록 하라는 설정.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Intent에 설정된 Component(여기서는 MainActivity)를 실행
            context?.startActivity(intent);
        }
    }
}