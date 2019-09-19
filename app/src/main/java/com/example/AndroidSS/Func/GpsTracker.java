package com.example.AndroidSS.Func;

import android.util.Log;
import android.Manifest;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.content.Context;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;


/**
 * REF  :: https://webnautes.tistory.com/1315
 * REF2 :: http://swlock.blogspot.com/2017/02/using-gps-example-in-android-gps.html
 * 출처 - https://gist.github.com/ertugrulozcan/70973144b1c582618a6c
 * 수정 - webnautes
 * Copy by SIEUN on 2019-09-17
 */

public class GpsTracker extends Service implements LocationListener
{
    private final Context mContext;
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;


    public GpsTracker(Context context)
    {
        this.mContext = context;
        getLocation();
    }


    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //GPS와 NET을 사용 가능 할 경우
            if (isGPSEnabled && isNetworkEnabled)
            {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                //PER이 없을 경우
                if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED)
                {
                    return null;
                }

                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.d("@@@", "" + e.toString());
        }

        return location;
    }

    public double getLatitude()
    {
        if (location != null)
        {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude()
    {
        if (location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public void stopUsingGPS()
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


    @Override
    public void onLocationChanged(Location location)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
}