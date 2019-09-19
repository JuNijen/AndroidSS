package com.example.AndroidSS.Func

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.content.DialogInterface
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.Manifest

import android.location.LocationManager
import android.location.Geocoder
import android.location.Address

import com.example.AndroidSS.R
import java.io.IOException
import java.util.Locale


//20190917 제작
//20190918 수정 - PER
//20190919 수정 - .java > .kt
//제작에 참고한 자료 ::
//https://webnautes.tistory.com/1315


//TODO:: 여기에 있는 내용 중 퍼미션에 해당하는 부분을 PermissionFunc.kt 로 옮길 필요가 있음.
class GPSFunc
{
    private var gpsTracker: GpsTracker? = null
    private val GPS_ENABLE_REQUEST_CODE = 2001


    fun callGetAdress(app_activity: AppCompatActivity) : String
    {
        return getAdress(app_activity)
    }

    private fun getAdress(app_activity: AppCompatActivity) : String
    {
        gpsTracker = GpsTracker(app_activity)

        //Latitude : 위도, Longitude : 경도
        return GetCurrentAddress(app_activity, gpsTracker!!.getLatitude(), gpsTracker!!.getLongitude())
    }

    private fun CheckPermissions(app_activity: AppCompatActivity)
    {
        //런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission( app_activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission( app_activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if ((hasFineLocationPermission != PackageManager.PERMISSION_GRANTED
                    && hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED))
        {
            val permissionFunc = PermissionFunc()

            //퍼미션 요청을 거절한 적이 있는지 체크.
            permissionFunc.CallCheckDeniedBefore(app_activity, MY_PERMISSION.E_ACCESS_FINE_LOCATION)
            permissionFunc.CallCheckDeniedBefore(app_activity, MY_PERMISSION.E_ACCESS_COARSE_LOCATION)
        }
    }


    fun GetCurrentAddress(app_activity: AppCompatActivity, latitude: Double, longitude: Double): String
    {
        //권한이 있는지 확인.
        CheckPermissions(app_activity)

        if (!CheckLocationServicesStatus(app_activity))
        {
            GpsNoticeDialog(app_activity)
        }

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(app_activity, Locale.getDefault())

        val addresses: List<Address>?
        var resultStr = ""

        try
        {
            addresses = geocoder.getFromLocation(latitude, longitude, 7)
        }
        catch (ioException: IOException)
        {
            //네트워크 문제
            Toast.makeText(app_activity, R.string.ERR_FROM_GPS_NETWORK, Toast.LENGTH_LONG).show()
            resultStr = app_activity.getString(R.string.ERR_FROM_GPS_NETWORK)
            return resultStr
        }
        catch (illegalArgumentException: IllegalArgumentException)
        {
            //잘못된 GPS 좌표
            Toast.makeText(app_activity, R.string.ERR_FROM_GPS_WRONG_LOCATION, Toast.LENGTH_LONG).show()
            resultStr = app_activity.getString(R.string.ERR_FROM_GPS_WRONG_LOCATION)
            return resultStr
        }

        if (addresses == null || addresses!!.size == 0)
        {
            //위치가 확인되지 않음
            Toast.makeText(app_activity, R.string.ERR_FROM_GPS_NO_LOCATION, Toast.LENGTH_LONG).show()
            resultStr = app_activity.getString(R.string.ERR_FROM_GPS_NO_LOCATION)
            return resultStr
        }

        val address = addresses!![0]
        return address.getAddressLine(0)
    }


    //GPS 활성화를 위한 Alert Dialog
    private fun GpsNoticeDialog(app_activity: AppCompatActivity)
    {
        //        GeneralFunc geleralFunc = new GeneralFunc();
        //        geleralFunc.CallCreateAlertDialog(this, getString(R.string.TEXT_GPS_DISABLED),
        //            getString(R.string.TEXT_GPS_NOTICE),true);

        app_activity.getString(R.string.TEXT_GPS_DISABLED)

        val builder = AlertDialog.Builder(app_activity)
        builder.setTitle(R.string.TEXT_GPS_DISABLED)
        builder.setMessage(R.string.TEXT_GPS_NOTICE)
        builder.setCancelable(true)

        builder.setPositiveButton(R.string.BTN_OK) {
                dialog, id ->
            val callGPSSettingIntent =
                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            app_activity.startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }

        builder.setNegativeButton(R.string.BTN_CANCEL, object : DialogInterface.OnClickListener
        {
            override fun onClick(dialog: DialogInterface, id: Int)
            {
                dialog.cancel()
            }
        })
        builder.create().show()
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
//    {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode)
//        {
//            GPS_ENABLE_REQUEST_CODE ->
//
//                //사용자가 GPS 활성 시켰는지 검사
//                if (CheckLocationServicesStatus())
//                {
//                    if (CheckLocationServicesStatus())
//                    {
//                        Log.d("@@@", "GPSFunc2.java - onActivityResult : GPS ON")
//                        Toast.makeText(this, R.string.TEXT_GPS_ON, Toast.LENGTH_LONG).show()
//                        return
//                    }
//                }
//        }
//    }

    fun CheckLocationServicesStatus(app_activity: AppCompatActivity): Boolean
    {
        val locationManager = app_activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ))
    }
}