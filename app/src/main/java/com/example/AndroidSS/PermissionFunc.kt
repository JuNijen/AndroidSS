package com.example.AndroidSS

import androidx.appcompat.app.AppCompatActivity

//하단부터 추가된 파일.
//Permission을 위하여 추가된 파일
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

//알림창을 위하여 추가된 파일
import android.app.AlertDialog
import android.widget.Toast


//20190916 제작
//TODO:: 이 클래스에 대한 개선이 필요.
//1. 메세지, 퍼미션 타입을 나눠서 분기 주기.
//2.


//내가아는... enum이... 아니네...
enum class MY_PERMISSION
{
    E_NONE,
    E_CALL_PHONE,
    E_ACCESS_FINE_LOCATION,
    E_ACCESS_COARSE_LOCATION
}

class PermissionFunc
{
    private var NONE = 0
    private var P_CALL_PHONE_ENABLE_REQUEST_CODE = 0
    private var P_ACESS_FINE_LOCATION_CODE = 0
    private var P_ACCESS_COARSE_LOCATION_CODE = 0


    // public fun ----------------------------------------------------------------------------------

    fun CallCheckPermission(app_activity: AppCompatActivity, per_type : MY_PERMISSION): Boolean
    {
        return CheckPermission(app_activity, per_type)
    }

    fun CallRequestPermission(app_activity: AppCompatActivity, per_type : MY_PERMISSION)
    {
        //안내를 먼저 띄우고 안내에서 퍼미션을 요청해야 함.
        RequestPermission(app_activity, per_type)
    }

    fun CallAlertDialog(app_activity: AppCompatActivity, per_type : MY_PERMISSION)
    {
        AlertDialog(app_activity, per_type)
    }

    //Permission에 맞는 Code(: Int)로 바꿔줌
    fun CallGetTypeCode(type_num : MY_PERMISSION) : Int
    {
        return GetTypeCode(type_num)
    }

    //Permission에 맞는 String(: String)로 바꿔줌
    fun CallGetTypeString(type_num : MY_PERMISSION) : String
    {
        return GetTypeString(type_num)
    }


    // private fun ---------------------------------------------------------------------------------


    private fun CheckPermission(app_activity: AppCompatActivity, per_type : MY_PERMISSION): Boolean
    {
        var bReady = false

        if (ContextCompat.checkSelfPermission(app_activity, CallGetTypeString(per_type)) != PackageManager.PERMISSION_GRANTED)
        {
            //퍼미션을 보유중이지 않을 경우
            //알림창을 별도로 띄워준다.
            CallAlertDialog(app_activity, per_type)
        }
        else
        {
            bReady = true
        }

        return bReady
    }

    private fun RequestPermission(app_activity: AppCompatActivity, per_type : MY_PERMISSION)
    {
        ActivityCompat.requestPermissions(app_activity, arrayOf(CallGetTypeString(per_type)), CallGetTypeCode(per_type))

        /*
        //제작에 참고한 자료
        //https://developer.android.com/training/permissions/requesting?hl=ko
        if (ActivityCompat.shouldShowRequestPermissionRationale(arg1,
                android.Manifest.permission.E_CALL_PHONE)) {
        }
        else
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(arg1, arrayOf(android.Manifest.permission.E_CALL_PHONE), MY_PERMISSIONS_REQUEST_CALL_PHONE)
        }
        */
    }

    private fun AlertDialog(app_activity: AppCompatActivity, per_type : MY_PERMISSION)
    {
        val builder = AlertDialog.Builder(app_activity)

        builder.setTitle(R.string.TEXT_NOTICE)
        builder.setMessage(R.string.TEXT_PERMISSION_NOTICE)
        builder.setCancelable(false)

        //요청 팝업을 띄워준다. 버튼을 누르면 리퀘스트.
        builder.setPositiveButton(R.string.BTN_OK) { dialogInterface, i -> CallRequestPermission(app_activity, per_type) }

        val dialog = builder.create()
        dialog.show()
    }

    //Permission에 맞는 String로 바꿔줌
    private fun GetTypeString(type_num : MY_PERMISSION) : String
    {
        var type_string = ""

        when (type_num) {
            //MY_PERMISSION.E_NONE -> type_string = getString(R.string.ERR_FROM_CODE)
            MY_PERMISSION.E_CALL_PHONE -> type_string = android.Manifest.permission.CALL_PHONE
            MY_PERMISSION.E_ACCESS_FINE_LOCATION -> type_string = android.Manifest.permission.ACCESS_FINE_LOCATION
            MY_PERMISSION.E_ACCESS_COARSE_LOCATION -> type_string = android.Manifest.permission.ACCESS_COARSE_LOCATION
        }

        return type_string
    }

    //Permission에 맞는 Code로 바꿔줌
    private fun GetTypeCode(type_num : MY_PERMISSION) : Int
    {
        var type_code = 0

        when (type_num) {
            MY_PERMISSION.E_NONE -> Toast.makeText(this, R.string.ERR_FROM_CODE, Toast.LENGTH_SHORT).show()
            MY_PERMISSION.E_CALL_PHONE -> type_code = P_CALL_PHONE_ENABLE_REQUEST_CODE
            MY_PERMISSION.E_ACCESS_FINE_LOCATION -> type_code = P_ACESS_FINE_LOCATION_CODE
            MY_PERMISSION.E_ACCESS_COARSE_LOCATION -> type_code = P_ACCESS_COARSE_LOCATION_CODE
        }

        return type_code
    }
}