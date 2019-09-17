package com.example.AndroidSS


import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;
import java.util.Locale;

import android.location.LocationListener;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationSettingsResult
import java.io.IOException

//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;


//20190917 제작
//제작에 참고한 자료 ::
//https://webnautes.tistory.com/1315
//수정에 참고한 자료 ::
//https://stackoverflow.com/questions/26559021/longitude-and-latitude-gpstracker-android-always-return-0-0

class GPSFunc
    :   GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult>
{
    //private GPSTracker gpsTracker;
    var gpsTracker : GpsTracker

    private var GPS_ENABLE_REQUEST_CODE = 2001;
    private var PERMISSIONS_REQUEST_CODE = 100;


    fun SetMain(arg_app_compact_activity : AppCompatActivity)
    {
            showDialogForLocationServiceSetting(arg_app_compact_activity);
    }

    //TODO :: 건드릴 수 있는 무언가에 달아줘야함
    fun GetMyPosition(arg_app_compact_activity : AppCompatActivity)
    {
        gpsTracker = GpsTracker(arg_app_compact_activity)

        var latitude = gpsTracker.getLatitude()
        var longitude = gpsTracker.getLongitude()

        var address = getCurrentAddress(latitude, longitude)

        //첫변수 context였음 (this)
        Toast.makeText(null, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
    }

    //TODO :: 퍼미션 체크해야함

    fun getCurrentAddress(latitude : Double, longitude : Double) : String
    {
        //var addresses : List<Address>
        val addresses = ArrayList<Address>()
        //지오코더... GPS를 주소로 변환
        //첫변수 context였음 (this)
        var geocoder = Geocoder(null, Locale.getDefault())
        var errStr : String

        addresses.clear()

        try
        {
            addresses.addAll(geocoder.getFromLocation(
                latitude,
                longitude,
                7))
        }
        catch (ioException : IOException)
        {
            //네트워크 문제
            //첫변수 context였음 (this)
            Toast.makeText(null, R.string.ERR_FROM_GPS_NETWORK, Toast.LENGTH_LONG).show();
            errStr = R.string.ERR_FROM_GPS_NETWORK.toString();
        }
        catch (illegalArgumentException : IllegalArgumentException)
        {
            //첫변수 context였음 (this)
            Toast.makeText(null, R.string.ERR_FROM_GPS_WRONG_LOCATION, Toast.LENGTH_LONG).show();
            errStr = R.string.ERR_FROM_GPS_WRONG_LOCATION.toString();
        }

        // addresses == null ||
        if (addresses.isNullOrEmpty())
        {
            //첫변수 context였음 (this)
            Toast.makeText(null, R.string.ERR_FROM_GPS_NO_LOCATION, Toast.LENGTH_LONG).show();
            errStr = R.string.ERR_FROM_GPS_NO_LOCATION.toString();

        }

        var address = addresses[0];
        return address.getAddressLine(0).toString()+"\n";
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting(arg_app_compact_activity : AppCompatActivity)
    {
        var builder = AlertDialog.Builder(arg_app_compact_activity)
        builder.setTitle(R.string.TEXT_GPS_DISABLED)
        builder.setMessage(R.string.TEXT_GPS_NOTICE)
        builder.setCancelable(true)

        builder.setPositiveButton(R.string.BTN_OK){
                dialogInterface, i->SetBTN(arg_app_compact_activity)}

        builder.create().show();
    }

    private fun SetBTN(arg_app_compact_activity : AppCompatActivity)
    {
        var callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(arg_app_compact_activity, callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE, null)
    }

    /*
    @Override
    protected fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case GPS_ENABLE_REQUEST_CODE:

            //사용자가 GPS 활성 시켰는지 검사
            if (checkLocationServicesStatus())
            {
                if (checkLocationServicesStatus())
                {

                    Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                    checkRunTimePermission();
                    return;
                }
            }
            break;
        }
    }
    */

    /*
    //이거 왜하는건지 모르겠음
    fun checkLocationServicesStatus() : Boolean
    {
        var locationManager : LocationManager

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    */
}
