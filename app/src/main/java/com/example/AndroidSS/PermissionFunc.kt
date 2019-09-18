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


//20190916 제작 - CALL ONLY
//20190917 수정 - 타입 분기
//20190918 수정 - GPS
//TODO:: 이 클래스에 대한 개선이 필요.
//1. 메세지, 퍼미션 타입을 나눠서 분기 주기.
//2.
// ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


//내가아는... enum이... 아니네...
enum class MY_PERMISSION
{
    E_CALL_PHONE,
    E_ACCESS_FINE_LOCATION,
    E_ACCESS_COARSE_LOCATION
}

class PermissionFunc
{
    private var P_CALL_PHONE_ENABLE_REQUEST_CODE = 0
    private var P_ACESS_FINE_LOCATION_CODE = 0
    private var P_ACCESS_COARSE_LOCATION_CODE = 0


    // public fun ----------------------------------------------------------------------------------

    //Check에는 Alart > Request 기능이 포함되어있다.
    fun CallCheckPermission(app_activity: AppCompatActivity, per_type: MY_PERMISSION): Boolean
    {
        return CheckPermission(app_activity, per_type)
    }

    fun CallRequestPermission(app_activity: AppCompatActivity, per_type: MY_PERMISSION)
    {
        RequestPermission(app_activity, per_type)
    }

    fun CallCheckDeniedBefore(app_activity: AppCompatActivity, per_type: MY_PERMISSION) : Boolean
    {
        return CheckDeniedBefore(app_activity, per_type)
    }

    fun CallAlertDialog(app_activity: AppCompatActivity, per_type: MY_PERMISSION)
    {
        AlertDialog(app_activity, per_type)
    }

    //Permission에 맞는 Code(: Int)로 바꿔줌
    fun CallGetTypeCode(type_num: MY_PERMISSION): Int
    {
        return GetTypeCode(type_num)
    }

    //Permission에 맞는 String(: String)로 바꿔줌
    fun CallGetTypeString(type_num: MY_PERMISSION): String
    {
        return GetTypeString(type_num)
    }


    // private fun ---------------------------------------------------------------------------------

    //Check에는 Alart > Request 기능이 포함되어있다.
    private fun CheckPermission(app_activity: AppCompatActivity, per_type: MY_PERMISSION): Boolean
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

    private fun RequestPermission(app_activity: AppCompatActivity, per_type: MY_PERMISSION)
    {
        ActivityCompat.requestPermissions(app_activity,
            arrayOf(CallGetTypeString(per_type)), CallGetTypeCode(per_type))
    }

    private fun CheckDeniedBefore(app_activity: AppCompatActivity, per_type: MY_PERMISSION) : Boolean
    {
        var bReady = false;

        //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지(3-1, 4-1) 경우가 있습니다.
        // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우

        //TODO::shouldSRPR을 대신할 기능을 찾아보아야한다.
        if (app_activity.shouldShowRequestPermissionRationale(CallGetTypeString(per_type)))
        {
            //요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명
            CallAlertDialog(app_activity, MY_PERMISSION.E_ACCESS_COARSE_LOCATION)
        }
        else
        {
            bReady = true
        }

        return bReady
    }

    private fun AlertDialog(app_activity: AppCompatActivity, per_type: MY_PERMISSION)
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
    private fun GetTypeString(type_num: MY_PERMISSION): String
    {
        var type_string = ""

        when (type_num)
        {
            MY_PERMISSION.E_CALL_PHONE -> type_string = android.Manifest.permission.CALL_PHONE
            MY_PERMISSION.E_ACCESS_FINE_LOCATION -> type_string = android.Manifest.permission.ACCESS_FINE_LOCATION
            MY_PERMISSION.E_ACCESS_COARSE_LOCATION -> type_string = android.Manifest.permission.ACCESS_COARSE_LOCATION
        }

        return type_string
    }

    //Permission에 맞는 Code로 바꿔줌
    private fun GetTypeCode(type_num: MY_PERMISSION): Int
    {
        var type_code = 0

        when (type_num)
        {
            MY_PERMISSION.E_CALL_PHONE -> type_code = P_CALL_PHONE_ENABLE_REQUEST_CODE
            MY_PERMISSION.E_ACCESS_FINE_LOCATION -> type_code = P_ACESS_FINE_LOCATION_CODE
            MY_PERMISSION.E_ACCESS_COARSE_LOCATION -> type_code = P_ACCESS_COARSE_LOCATION_CODE
        }

        return type_code
    }
}