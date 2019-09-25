package com.example.AndroidSS.Service

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

import android.telephony.SmsMessage
import android.provider.Telephony.Threads
import androidx.appcompat.app.AppCompatActivity
import com.example.AndroidSS.Func.TTSFunc

import java.text.MessageFormat
import java.io.InputStreamReader
import java.io.InputStream
import java.io.BufferedReader
import java.io.IOException
import java.lang.Long


//20190924 제작
//참고자료 ::
//https://g-y-e-o-m.tistory.com/26
class MySMSReceiver : BroadcastReceiver()
{
//    lateinit var contentResolver : ContentResolver
//    val projection = arrayOf("*")
//    var uri = Uri.parse("content://mms-sms/conversations/")
//    var query = contentResolver.query(uri, projection, null, null, null)

    //특정 스레드에 대한 적절한 메시지 목록을 얻으려면 Mms 및 Sms 공급자 모두에 대해
    //쿼리를 수행 한 다음 결과를 하나의 목록으로 결합한 다음, 날짜별로 정렬해야한다.
    val ALL_THREADS_PROJECTION = arrayOf(
        Threads._ID,
        Threads.DATE,
        Threads.MESSAGE_COUNT,
        Threads.RECIPIENT_IDS,
        Threads.SNIPPET,
        Threads.SNIPPET_CHARSET,
        Threads.READ,
        Threads.ERROR,
        Threads.HAS_ATTACHMENT
    )

    override fun onReceive(context: Context, intent: Intent)
    {
//        contentResolver =  context.getContentResolver()
        // 수신되었을 때 호출되는 콜백 메서드
        // 매개변수 intent의 액션에 방송의 '종류'가 들어있고 필드에는 '추가정보' 가 들어있음

        Log.i("@@@onReceive", "@@@무언가 동작했습니다.")
        
        parsingMessage(intent)
    }

    private  fun parsingMessage(intent: Intent)
    {
        // SMS 메시지를 파싱합니다.
        val bundle = intent.extras
        if (bundle != null)
        { // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨

            //TODO::이부분 어떻게 스트링 미리 정의 해 둘 수 없을까?
            //문자 메시지는 pdus란 종류 값으로 들어있음
            val pdus = bundle.get("pdus") as Array<Any>?

            val msgs = arrayOfNulls<SmsMessage>(pdus!!.size)
            for (i in msgs.indices)
            {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage
                    .createFromPdu(pdus[i] as ByteArray)

                val sender = msgs[i]?.getOriginatingAddress()
                val content = msgs[i]?.getMessageBody().toString()

                Log.i("@@@sender", sender!!)
                Log.i("@@@content", content)

                if (sender == "01000000000")
                {
                    val startIdx = content.indexOf("[")
                    val endIdx = content.indexOf("]")
                    val authNumber = content.substring(startIdx + 1, endIdx)
                    Log.i("@@@authNumber", authNumber)
                }
            }
        }
    }
//
//    //발신자 주소를 얻는 방법
//    //다음과 같이 content://mms/xxx/addr 공급자를 사용해야합니다
//    //content://mms/xxx/addr 여기서 xxx 는 MMS의 ID입니다.
//    private fun getAddressNumber(id: Int): String?
//    {
//        val selectionAdd = "msg_id=$id"
//        val uriStr = MessageFormat.format("content://mms/{0}/addr", id)
//        val uriAddress = Uri.parse(uriStr)
//        val cAdd = contentResolver.query(
//            uriAddress, null,
//            selectionAdd, null, null
//        )
//        var name: String? = null
//        if (cAdd!!.moveToFirst())
//        {
//            do
//            {
//                val number = cAdd!!.getString(cAdd!!.getColumnIndex("address"))
//                if (number != null)
//                {
//                    try
//                    {
//                        Long.parseLong(number!!.replace("-", ""))
//                        name = number
//                    }
//                    catch (nfe: NumberFormatException)
//                    {
//                        if (name == null)
//                        {
//                            name = number
//                        }
//                    }
//
//                }
//            }
//            while (cAdd!!.moveToNext())
//        }
//        if (cAdd != null)
//        {
//            cAdd!!.close()
//        }
//
//        return name
//    }
//
//    //SMS와 MMS를 구별하는 방법
//    //가상 열 MmsSms.TYPE_DISCRIMINATOR_COLUMN 은 쿼리 투영에서 요청할 수 있습니다.
//    //이 값은 행이 나타내는 메시지가 각각 MMS 메시지인지 또는 SMS 메시지인지에 따라
//    //"mms"또는 "sms"입니다.
//    private fun checkMmsSms(id: Int)
//    {
//        val contentResolver = contentResolver
//        val projection = arrayOf("_id", "ct_t")
//        val uri = Uri.parse("content://mms-sms/conversations/")
//        val query = contentResolver.query(uri, projection, null, null, null)
//        if (query!!.moveToFirst())
//        {
//            do
//            {
//                val string = query!!.getString(query!!.getColumnIndex("ct_t"))
//                if ("application/vnd.wap.multipart.related" == string)
//                {
//                    // it's MMS
//                    readSms(id)
//                }
//                else
//                {
//                    // it's SMS
//                    readMms(id)
//                }
//            }
//            while (query!!.moveToNext())
//        }
//    }
//
//    //SMS에서 데이터를 얻는 방법
//    //SMS의 ID를 가지고 있으므로 다음 작업 만 수행하면됩니다.
//    private fun readSms(id: Int)
//    {
//        val selection = "_id = $id"
//        val uri = Uri.parse("content://sms")
//        val cursor = contentResolver.query(uri, null, selection, null, null)
//        val phone = cursor!!.getString(cursor.getColumnIndex("address"))
//        val type = cursor!!.getInt(cursor.getColumnIndex("type"))// 2 = sent, etc.
//        val date = cursor!!.getString(cursor.getColumnIndex("date"))
//        val body = cursor!!.getString(cursor.getColumnIndex("body"))
//    }
//
//    //MMS에서 텍스트 콘텐츠를 얻는 방법
//    //여기에 content://mms/part ...를 사용해야 content://mms/part 예를 들면 다음과 같습니다.
//    private fun readMms(mmsId: Int)
//    {
//        val selectionPart = "mid=$mmsId"
//        val uri = Uri.parse("content://mms/part")
//        val cursor = contentResolver.query(
//            uri, null,
//            selectionPart, null, null
//        )
//        if (cursor!!.moveToFirst())
//        {
//            do
//            {
//                val partId = cursor!!.getString(cursor.getColumnIndex("_id"))
//                val type = cursor!!.getString(cursor.getColumnIndex("ct"))
//                if ("text/plain" == type)
//                {
//                    val data = cursor!!.getString(cursor.getColumnIndex("_data"))
//                    val body: String
//                    if (data != null)
//                    {
//                        // implementation of this method below
//                        body = getMmsText(partId)
//                    }
//                    else
//                    {
//                        body = cursor!!.getString(cursor.getColumnIndex("text"))
//                    }
//                }
//            }
//            while (cursor.moveToNext())
//        }
//    }
//
//    //그것은 텍스트의 다른 부분을 포함 할 수 있지만 일반적으로 하나뿐입니다.
//    //따라서 루프를 제거하려면 대부분의 경우 작동합니다. 다음은 getMmsText 메소드의 모습입니다.
//    private fun getMmsText(id: String): String
//    {
//        val partURI = Uri.parse("content://mms/part/$id")
//        var `is`: InputStream? = null
//        val sb = StringBuilder()
//        try
//        {
//            `is` = contentResolver.openInputStream(partURI)
//            if (`is` != null)
//            {
//                val isr = InputStreamReader(`is`, "UTF-8")
//                val reader = BufferedReader(isr)
//                var temp = reader.readLine()
//                while (temp != null)
//                {
//                    sb.append(temp)
//                    temp = reader.readLine()
//                }
//            }
//        }
//        catch (e: IOException)
//        {
//        }
//        finally
//        {
//            if (`is` != null)
//            {
//                try
//                {
//                    `is`!!.close()
//                }
//                catch (e: IOException)
//                {
//                }
//
//            }
//        }
//
//        return sb.toString()
//    }
}