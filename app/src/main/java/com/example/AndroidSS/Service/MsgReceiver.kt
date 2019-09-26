package com.example.AndroidSS.Service

import android.telephony.SmsMessage
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.example.AndroidSS.Func.TTSFunc


//20190924 제작
//20190925 수정
//참고자료 ::
//https://g-y-e-o-m.tistory.com/26
class MsgReceiver : BroadcastReceiver()
{
    lateinit var ttsFunc: TTSFunc
    private val TAG = "@@@MsgReceiver : "


    override fun onReceive(context: Context, intent: Intent)
    {
        // 수신되었을 때 호출되는 콜백 메서드
        // 매개변수 intent의 액션에 방송의 '종류'가 들어있고 필드에는 '추가정보' 가 들어있음

        Log.i(TAG, ":onReceive")
        
        parsingMessage(context, intent)
    }

    private fun parsingMessage(context: Context, intent: Intent)
    {
        val bundle = intent.extras
        if (bundle != null)
        {
            //TODO::이부분 어떻게 스트링 미리 정의 해 둘 수 없을까?
            //실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨
            //문자 메시지는 pdus란 종류 값으로 들어있다.
            val arrPdus = bundle.get("pdus") as Array<Any>?
            val arrMsgs = arrayOfNulls<SmsMessage>(arrPdus!!.size)

            var stringList : ArrayList<String> = ArrayList()

            for (repeatNum in arrMsgs.indices)
            {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                arrMsgs[repeatNum] = SmsMessage
                    .createFromPdu(arrPdus[repeatNum] as ByteArray)

                val strSender = arrMsgs[repeatNum]?.originatingAddress
                val strContent = arrMsgs[repeatNum]?.messageBody.toString()

                Log.i(TAG, strSender!!)
                Log.i(TAG, strContent)

                stringList.add(strContent)

                //TODO::왜팅기는지 잘 모르겠음. 0000으로 보내는 건 지양하는 것으로.. ...
                if (strSender == "01000000000")
                {
                    senderIs01000000000(strContent)
                }
            }

            var ttsServiceIntent = Intent(context, MsgTtsService::class.java)
            ttsServiceIntent.putExtra("message", stringList)
            context.startService(ttsServiceIntent)

            stringList.clear()
        }
    }

    private fun senderIs01000000000(messageText : String)
    {
        val startIdx = messageText.indexOf("[")
        val endIdx = messageText.indexOf("]")
        val authNumber = messageText.substring(startIdx + 1, endIdx)
        Log.i(TAG, authNumber)
    }
}