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


    fun callGetAdress(appCompactActivity: AppCompatActivity) : String
    {
        return getAdress(appCompactActivity)
    }

    private fun getAdress(appCompactActivity: AppCompatActivity) : String
    {
        gpsTracker = GpsTracker(appCompactActivity)

        //Latitude : 위도, Longitude : 경도
        return GetCurrentAddress(appCompactActivity, gpsTracker!!.getLatitude(), gpsTracker!!.getLongitude())
    }

    private fun CheckPermissions(appCompactActivity: AppCompatActivity)
    {
        //런타임 퍼미션 처리
        // 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission( appCompactActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission( appCompactActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if ((hasFineLocationPermission != PackageManager.PERMISSION_GRANTED
                    && hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED))
        {
            val permissionFunc = PermissionFunc()

            //퍼미션 요청을 거절한 적이 있는지 체크.
            permissionFunc.CallCheckDeniedBefore(appCompactActivity, MY_PERMISSION.E_ACCESS_FINE_LOCATION)
            permissionFunc.CallCheckDeniedBefore(appCompactActivity, MY_PERMISSION.E_ACCESS_COARSE_LOCATION)
        }
    }


    fun GetCurrentAddress(appCompactActivity: AppCompatActivity, latitude: Double, longitude: Double): String
    {
        //권한이 있는지 확인.
        CheckPermissions(appCompactActivity)

        if (!CheckLocationServicesStatus(appCompactActivity))
        {
            GpsNoticeDialog(appCompactActivity)
        }

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(appCompactActivity, Locale.getDefault())

        val addresses: List<Address>?
        var resultStr = ""

        try
        {
            addresses = geocoder.getFromLocation(latitude, longitude, 7)
        }
        catch (ioException: IOException)
        {
            //네트워크 문제
            Toast.makeText(appCompactActivity, R.string.ERR_FROM_GPS_NETWORK, Toast.LENGTH_LONG).show()
            resultStr = appCompactActivity.getString(R.string.ERR_FROM_GPS_NETWORK)
            return resultStr
        }
        catch (illegalArgumentException: IllegalArgumentException)
        {
            //잘못된 GPS 좌표
            Toast.makeText(appCompactActivity, R.string.ERR_FROM_GPS_WRONG_LOCATION, Toast.LENGTH_LONG).show()
            resultStr = appCompactActivity.getString(R.string.ERR_FROM_GPS_WRONG_LOCATION)
            return resultStr
        }

        if (addresses == null || addresses!!.size == 0)
        {
            //위치가 확인되지 않음
            Toast.makeText(appCompactActivity, R.string.ERR_FROM_GPS_NO_LOCATION, Toast.LENGTH_LONG).show()
            resultStr = appCompactActivity.getString(R.string.ERR_FROM_GPS_NO_LOCATION)
            return resultStr
        }

        val address = addresses!![0]
        return address.getAddressLine(0)
    }


    //GPS 활성화를 위한 Alert Dialog
    private fun GpsNoticeDialog(appCompactActivity: AppCompatActivity)
    {
        //        GeneralFunc geleralFunc = new GeneralFunc();
        //        geleralFunc.CallCreateAlertDialog(this, getString(R.string.TEXT_GPS_DISABLED),
        //            getString(R.string.TEXT_GPS_NOTICE),true);

        appCompactActivity.getString(R.string.TEXT_GPS_DISABLED)

        val builder = AlertDialog.Builder(appCompactActivity)
        builder.setTitle(R.string.TEXT_GPS_DISABLED)
        builder.setMessage(R.string.TEXT_GPS_NOTICE)
        builder.setCancelable(true)

        builder.setPositiveButton(R.string.BTN_OK) {
                dialog, id ->
            val callGPSSettingIntent =
                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            appCompactActivity.startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
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


    fun CheckLocationServicesStatus(appCompactActivity: AppCompatActivity): Boolean
    {
        val locationManager = appCompactActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ))
    }
}