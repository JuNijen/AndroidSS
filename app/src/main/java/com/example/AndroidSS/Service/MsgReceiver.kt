package com.example.AndroidSS.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import android.telephony.SmsMessage
import com.example.AndroidSS.Func.TTSFunc


//20190924 제작
//20190925 수정
//참고자료 ::
//https://g-y-e-o-m.tistory.com/26
class msgReceiver : BroadcastReceiver()
{
    lateinit var ttsFunc: TTSFunc

    override fun onReceive(context: Context, intent: Intent)
    {
        // 수신되었을 때 호출되는 콜백 메서드
        // 매개변수 intent의 액션에 방송의 '종류'가 들어있고 필드에는 '추가정보' 가 들어있음

        Log.i("@@@msgReceiver", ":onReceive")
        
        parsingMessage(intent)
    }

    private fun parsingMessage(intent: Intent)
    {
        val bundle = intent.extras
        if (bundle != null)
        {
            //TODO::이부분 어떻게 스트링 미리 정의 해 둘 수 없을까?
            //실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨
            //문자 메시지는 pdus란 종류 값으로 들어있다.
            val pdus = bundle.get("pdus") as Array<Any>?
            val msgs = arrayOfNulls<SmsMessage>(pdus!!.size)

            for (repeatNum in msgs.indices)
            {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[repeatNum] = SmsMessage
                    .createFromPdu(pdus[repeatNum] as ByteArray)

                val sender = msgs[repeatNum]?.getOriginatingAddress()
                val content = msgs[repeatNum]?.getMessageBody().toString()

                Log.i("@@@msgReceiver:num: ", sender!!)
                Log.i("@@@msgReceiver:data: ", content)

                //TODO::왜팅기는지 잘 모르겠음. 0000으로 보내는 건 지양하는 것으로.. ...
                if (sender == "01000000000")
                {
                    senderIs01000000000(content)
                }
            }
        }
    }

    private fun senderIs01000000000(content : String)
    {
        val startIdx = content.indexOf("[")
        val endIdx = content.indexOf("]")
        val authNumber = content.substring(startIdx + 1, endIdx)
        Log.i("@@@msgReceiver:authNum", authNumber)
    }
}