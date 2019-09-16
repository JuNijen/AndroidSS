package com.example.nijentestapplication0916

import androidx.appcompat.app.AppCompatActivity

//하단부터 추가된 파일.
//Permission을 위하여 추가된 파일
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

//알림창을 위하여 추가된 파일
import android.app.AlertDialog


class PermissionActivity
{

    enum class MY_PERMISSION
    {
        NONE,
        CALL_PHONE,
        CAMERA
    }

    private val PERMISSIONS_STORAGE = arrayOf<String>(
        android.Manifest.permission.CALL_PHONE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )


    private var MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private var MY_PERMISSIONS_REQUEST_CAMERA = 0;


    fun CallCheckPermission(arg_app_compact_activity : AppCompatActivity) : Boolean
    {
        return CheckPermission(arg_app_compact_activity)
    }

    fun CallRequestPermission(arg_app_compact_activity : AppCompatActivity)
    {
        RequestPermission(arg_app_compact_activity)
    }

    fun CallAlertDialog(arg_app_compact_activity : AppCompatActivity, arg_str : String)
    {
        AlertDialog(arg_app_compact_activity)
    }

    private fun CheckPermission(arg_app_compact_activity : AppCompatActivity) : Boolean
    {
        var bReady = false

        if (ContextCompat.checkSelfPermission(arg_app_compact_activity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            //퍼미션을 보유중이지 않을 경우
            //알림창을 별도로 띄워준다.
            AlertDialog(arg_app_compact_activity)
        }
        else
        {
            bReady = true
        }

        return bReady
    }

    private fun RequestPermission(arg_app_compact_activity : AppCompatActivity)
    {
        ActivityCompat.requestPermissions(arg_app_compact_activity,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                MY_PERMISSIONS_REQUEST_CALL_PHONE)


        /*
        if (ActivityCompat.shouldShowRequestPermissionRationale(arg1,
                android.Manifest.permission.CALL_PHONE)) {
        }
        else
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(arg1, arrayOf(android.Manifest.permission.CALL_PHONE), MY_PERMISSIONS_REQUEST_CALL_PHONE)
        }
        */
    }

    private fun AlertDialog(arg_app_compact_activity : AppCompatActivity) {
        val builder = AlertDialog.Builder(arg_app_compact_activity)

        builder.setTitle(R.string.TEXT_NOTICE)
        builder.setMessage(R.string.TEXT_PERMISSION_NOTICE)
        builder.setCancelable(false)

        builder.setPositiveButton(R.string.BTN_OK){dialogInterface, i ->RequestPermission(arg_app_compact_activity)}
        //요청 팝업을 띄워준다.

        val dialog = builder.create()
        dialog.show()
    }
}