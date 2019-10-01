package com.example.AndroidSS.Func

import android.provider.ContactsContract.PhoneLookup
import android.content.Context
import android.net.Uri


//20190926 제작
//참고자료 ::
//http://effcode.com/coding/android-find-contact-id-by-phone-number/
//20191001 수정
//참고자료 :: https://code-examples.net/ko/q/2efcc5
class ContactFunc
{
    // public fun ----------------------------------------------------------------------------------


    fun callGetContactName(context: Context, phoneNumber: String): String?
    {
        return getContactName(context, phoneNumber)
    }

    // private fun ---------------------------------------------------------------------------------


    //20191001 제작
    //참고자료 ::
    //https://code-examples.net/ko/q/2efcc5
    fun getContactName(context: Context, phoneNumber: String): String?
    {
        val cr = context.contentResolver
        val uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cursor =
            cr.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null) ?: return null
        var contactName: String? = null

        if (cursor.moveToFirst())
        {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
        }

        if (cursor != null && !cursor.isClosed)
        {
            cursor.close()
        }

        return contactName
    }
}