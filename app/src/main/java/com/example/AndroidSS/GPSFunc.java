package com.example.AndroidSS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.content.DialogInterface;

import android.location.Geocoder;
import android.location.Address;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;
import android.Manifest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.io.IOException;


//20190917 제작
//20190918 수정 - PER
//제작에 참고한 자료 ::
//https://webnautes.tistory.com/1315


//TODO:: 여기에 있는 내용 중 퍼미션에 해당하는 부분을 PermissionFunc.kt 로 옮길 필요가 있음.
public class GPSFunc extends AppCompatActivity
{
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_gps);


        if (!checkLocationServicesStatus())
        {
            showDialogForLocationServiceSetting();
        }
        else
        {
            checkRunTimePermission();
        }

        final TextView textview_address = findViewById(R.id.textview);


        Button ShowLocationButton = findViewById(R.id.button);
        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                checkRunTimePermission();

                gpsTracker = new GpsTracker(GPSFunc.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                textview_address.setText(address);

                Toast.makeText(GPSFunc.this, "현재위치 \n위도 "
                        + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
            }
        });
    }

    void checkRunTimePermission()
    {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED)
        {
            PermissionFunc permissionFunc = new PermissionFunc();

            //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다.
            if(permissionFunc.CallCheckDeniedBefore(this,MY_PERMISSION.E_ACCESS_COARSE_LOCATION))
            {
                permissionFunc.CallRequestPermission(this, MY_PERMISSION.E_ACCESS_COARSE_LOCATION);
            }
        }
    }


    public String getCurrentAddress(double latitude, double longitude)
    {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(GPSFunc.this, Locale.getDefault());

        List<Address> addresses;
        String resultStr = "";

        try
        {
            addresses = geocoder.getFromLocation( latitude, longitude, 7);
        }
        catch (IOException ioException)
        {
            //네트워크 문제
            Toast.makeText(this, R.string.ERR_FROM_GPS_NETWORK, Toast.LENGTH_LONG).show();
            resultStr = getString(R.string.ERR_FROM_GPS_NETWORK);
            return resultStr;
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            //잘못된 GPS 좌표
            Toast.makeText(this, R.string.ERR_FROM_GPS_WRONG_LOCATION, Toast.LENGTH_LONG).show();
            resultStr = getString(R.string.ERR_FROM_GPS_WRONG_LOCATION);
            return resultStr;
        }

        if (addresses == null || addresses.size() == 0)
        {
            //위치가 확인되지 않음
            Toast.makeText(this, R.string.ERR_FROM_GPS_NO_LOCATION, Toast.LENGTH_LONG).show();
            resultStr = getString(R.string.ERR_FROM_GPS_NO_LOCATION);
            return resultStr;
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();
    }


    //GPS 활성화를 위한 메소드
    private void showDialogForLocationServiceSetting()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.TEXT_GPS_DISABLED);
        builder.setMessage(R.string.TEXT_GPS_NOTICE);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.BTN_OK, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton(R.string.BTN_CANCEL, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
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
                        Log.d("@@@", "GPSFunc.java - onActivityResult : GPS ON");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}