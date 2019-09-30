package com.example.AndroidSS.Func

import android.provider.ContactsContract
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity


//20190926 제작
//참고자료 ::
//http://effcode.com/coding/android-find-contact-id-by-phone-number/
class ContactFunc
{
    // public fun ----------------------------------------------------------------------------------

    fun callContactIdByPhoneNumber(context: Context, phoneNumber: String): String
    {
        return contactIdByPhoneNumber(context, phoneNumber)
    }


    // private fun ---------------------------------------------------------------------------------

    private fun contactIdByPhoneNumber(context: Context, phoneNumber: String): String
    {
        var uri : Uri
        var contactID = ""
        var contentResolver: ContentResolver?


        //전화번호에 에러가 없는지 체크.
        if (!phoneNumber.isNullOrEmpty())
        {
            contentResolver = context.contentResolver
            uri = Uri . withAppendedPath (ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))

            var projection = arrayOf(ContactsContract.PhoneLookup._ID)
            //projectionList?.add(ContactsContract.PhoneLookup._ID)
            //projection = projectionList as Array<String>
            //var projection = String[] { ContactsContract.PhoneLookup._ID }

            //여기까지는 잘 들어오는데 대충 여기서 좆되고있음 왜인지 당췌 모르겠음
            var cursor = contentResolver.query (uri, projection, null, null, null)

            if (cursor != null)
            {
                while (cursor?.moveToNext())
                {
                    contactID = cursor?.getString(cursor?.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID))
                }
                cursor?.close()
            }
        }
        return contactID
    }
}