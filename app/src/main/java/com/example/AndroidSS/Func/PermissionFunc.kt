package com.example.AndroidSS.Func

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.util.Log

import com.example.AndroidSS.R


//20190916 제작 - CALL ONLY
//20190917 수정 - 타입 분기 추가
//20190918 수정 - GPS 추가
// ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


//내가아는... enum이... 아니네...
enum class MY_PERMISSION
{
    E_NONE,
    E_CALL_PHONE,
    E_ACCESS_FINE_LOCATION,
    E_ACCESS_COARSE_LOCATION,
    E_RECORD_AUDIO,
    E_WRITE_EXTERNAL_STORAGE,
    E_INTERNET,
    E_READ_SMS,
    E_RECEIVE_SMS,
    E_READ_CONTACTS
}

class PermissionFunc
{
    private val TAG = "@@@PermissionFunc : "

    //TODO :: 네이밍에 문제가 있다는 것 같은데 잘 모르겠음. 추후 체크.
    private var P_CALL_PHONE_ENABLE = 0
    private var P_ACESS_FINE_LOCATION_ENABLE = 0
    private var P_ACCESS_COARSE_LOCATION_ENABLE = 0
    private var P_RECORD_AUDIO_ENABLE = 0
    private var P_WRITE_EXTERNAL_STORAGE_ENABLE = 0
    private var P_INTERNET_ENABLE = 0
    private var P_READ_SMS_ENABLE = 0
    private var P_RECEIVE_SMS_ENABLE = 0
    private var P_READ_CONTACTS = 0



    // public fun ----------------------------------------------------------------------------------

    //Check에는 Alart > Request 기능이 포함되어있다.
    fun callCheckPermission(appCompactActivity: AppCompatActivity, perType: MY_PERMISSION): Boolean
    {
        return checkPermission(appCompactActivity, perType)
    }

    fun callRequestPermission(appCompactActivity: AppCompatActivity, perType: MY_PERMISSION)
    {
        requestPermission(appCompactActivity, perType)
    }

    fun callCheckDeniedBefore(appCompactActivity: AppCompatActivity, perType: MY_PERMISSION) : Boolean
    {
        return checkDeniedBefore(appCompactActivity, perType)
    }

    //Permission에 맞는 Code(: Int)로 바꿔줌
    fun callGetTypeCode(typeNum: MY_PERMISSION): Int
    {
        return getTypeCode(typeNum)
    }

    //Permission에 맞는 String(: String)로 바꿔줌
    fun callGetTypeString(typeNum: MY_PERMISSION): String
    {
        return getTypeString(typeNum)
    }


    // private fun ---------------------------------------------------------------------------------

    //Check에는 Alart > Request 기능이 포함되어있다.
    private fun checkPermission(appCompactActivity: AppCompatActivity, perType: MY_PERMISSION): Boolean
    {
        var bReady = false

        if (ContextCompat.checkSelfPermission(appCompactActivity, callGetTypeString(perType))
                                                            != PackageManager.PERMISSION_GRANTED)
        {
            //퍼미션을 보유중이지 않을 경우
            //알림창을 별도로 띄워준다.
            GeneralFunc().CallCreateAlertDialog(appCompactActivity, appCompactActivity.getString(R.string.TEXT_NOTICE),
                    appCompactActivity.getString(R.string.TEXT_PERMISSION_NOTICE, getPermissionName(appCompactActivity, perType)),
                false, perType)
        }
        else
        {
            bReady = true
        }

        return bReady
    }

    private fun requestPermission(appCompactActivity: AppCompatActivity, perType: MY_PERMISSION)
    {
        ActivityCompat.requestPermissions(appCompactActivity,
            arrayOf(callGetTypeString(perType)), callGetTypeCode(perType))
    }

    private fun checkDeniedBefore(appCompactActivity: AppCompatActivity, perType: MY_PERMISSION) : Boolean
    {
        var bReady = false

        //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지(3-1, 4-1) 경우가 있습니다.
        // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우

        //TODO::shouldSRPR을 대신할 기능을 찾아보아야한다.
        if (appCompactActivity.shouldShowRequestPermissionRationale(callGetTypeString(perType)))
        {
            //요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명
            GeneralFunc()
                .CallCreateAlertDialog(appCompactActivity, appCompactActivity.getString(R.string.TEXT_NOTICE),
                    appCompactActivity.getString(R.string.TEXT_PERMISSION_NOTICE, getPermissionName(appCompactActivity, perType)),
                    false, perType)
        }
        else
        {
            bReady = true
        }

        return bReady
    }

    //Permission에 맞는 String로 바꿔줌
    private fun getTypeString(typeNum: MY_PERMISSION): String
    {
        var typeString = ""

        when (typeNum)
        {
            MY_PERMISSION.E_CALL_PHONE -> typeString = android.Manifest.permission.CALL_PHONE
            MY_PERMISSION.E_ACCESS_FINE_LOCATION -> typeString = android.Manifest.permission.ACCESS_FINE_LOCATION
            MY_PERMISSION.E_ACCESS_COARSE_LOCATION -> typeString = android.Manifest.permission.ACCESS_COARSE_LOCATION
            MY_PERMISSION.E_RECORD_AUDIO -> typeString = android.Manifest.permission.RECORD_AUDIO
            MY_PERMISSION.E_WRITE_EXTERNAL_STORAGE -> typeString = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            MY_PERMISSION.E_INTERNET -> typeString = android.Manifest.permission.INTERNET
            MY_PERMISSION.E_READ_SMS -> typeString = android.Manifest.permission.READ_SMS
            MY_PERMISSION.E_RECEIVE_SMS -> typeString = android.Manifest.permission.RECEIVE_SMS
            MY_PERMISSION.E_READ_CONTACTS -> typeString = android.Manifest.permission.READ_CONTACTS
        }

        return typeString
    }

    //Permission에 맞는 Code로 바꿔줌
    private fun getTypeCode(typeNum: MY_PERMISSION): Int
    {
        var typeCode = 0

        when (typeNum)
        {
            MY_PERMISSION.E_CALL_PHONE -> typeCode = P_CALL_PHONE_ENABLE
            MY_PERMISSION.E_ACCESS_FINE_LOCATION -> typeCode = P_ACESS_FINE_LOCATION_ENABLE
            MY_PERMISSION.E_ACCESS_COARSE_LOCATION -> typeCode = P_ACCESS_COARSE_LOCATION_ENABLE
            MY_PERMISSION.E_RECORD_AUDIO -> typeCode = P_RECORD_AUDIO_ENABLE
            MY_PERMISSION.E_WRITE_EXTERNAL_STORAGE -> typeCode = P_WRITE_EXTERNAL_STORAGE_ENABLE
            MY_PERMISSION.E_INTERNET -> typeCode = P_INTERNET_ENABLE
            MY_PERMISSION.E_READ_SMS -> typeCode = P_READ_SMS_ENABLE
            MY_PERMISSION.E_RECEIVE_SMS -> typeCode = P_RECEIVE_SMS_ENABLE
            MY_PERMISSION.E_READ_CONTACTS -> typeCode = P_READ_CONTACTS
        }

        return typeCode
    }

    //Permission에 맞는 Name로 바꿔줌
    private fun getPermissionName(appCompactActivity: AppCompatActivity, typeNum: MY_PERMISSION): String
    {
        //TODO:: 이거 하나때문에 app_activity를 받아오는것이 옳은지 생각 해 봐야한다.
        var nameString = ""

        when (typeNum)
        {
            MY_PERMISSION.E_CALL_PHONE ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_CALL_PHONE)

            MY_PERMISSION.E_ACCESS_FINE_LOCATION ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_ACCESS_FINE_LOCATION)

            MY_PERMISSION.E_ACCESS_COARSE_LOCATION ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_ACCESS_COARSE_LOCATION)

            MY_PERMISSION.E_RECORD_AUDIO ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_RECORD_AUDIO)

            MY_PERMISSION.E_WRITE_EXTERNAL_STORAGE ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_WRITE_EXTERNAL_STORAGE)

            MY_PERMISSION.E_INTERNET ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_INTERNET)

            MY_PERMISSION.E_READ_SMS ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_READ_SMS)

            MY_PERMISSION.E_RECEIVE_SMS ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_RECEIVE_SMS)

            MY_PERMISSION.E_READ_CONTACTS ->
                nameString = appCompactActivity.getString(R.string.TEXT_PERMISSION_READ_CONTEXT)

            MY_PERMISSION.E_NONE -> Log.i(TAG, appCompactActivity.getString(R.string.ERR_FROM_CODE))
        }

        return nameString
    }
}