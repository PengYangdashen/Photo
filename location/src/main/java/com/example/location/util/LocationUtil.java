package com.example.location.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.location.application.App;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LocationUtil {

    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean passive_enabled = false;

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
            lm.removeUpdates(locationListenerpassive);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerpassive);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerpassive = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };


    public boolean getLocation(Context context, LocationResult result) {
        // I use LocationResult callback class to pass location value from MyLocation to user code.
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationResult = result;
        //是否可用
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            passive_enabled = lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
        } catch (Exception ex) {
        }

        //如果gps和网络还有wifi不行就不开监听
        if (!gps_enabled && !network_enabled && !passive_enabled)
            return false;

        //压制警告
        if (ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        if (passive_enabled)
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListenerpassive);

        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 20000);
        return true;
    }
    public void cancelTimer() {
        timer1.cancel();
        lm.removeUpdates(locationListenerGps);
        lm.removeUpdates(locationListenerNetwork);
        lm.removeUpdates(locationListenerpassive);
    }
    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);
            lm.removeUpdates(locationListenerpassive);

            Location net_loc=null, gps_loc=null,passive_loc=null;

            //压制警告
            if (ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(gps_enabled)
                gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(network_enabled)
                net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(passive_enabled)
                passive_loc=lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            //使用最近的一条
            if(gps_loc!=null && net_loc!=null&&passive_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime()&&gps_loc.getTime()>passive_loc.getTime()){
                    locationResult.gotLocation(gps_loc);
                } else if (net_loc.getTime()>gps_loc.getTime()&&net_loc.getTime()>passive_loc.getTime()){
                    locationResult.gotLocation(net_loc);
                }else {
                    locationResult.gotLocation(passive_loc);
                }
                return;
            }

            if(gps_loc!=null){
                locationResult.gotLocation(gps_loc);
                return;
            }
            if(net_loc!=null){
                locationResult.gotLocation(net_loc);
                return;
            }
            if(passive_loc!=null){
                locationResult.gotLocation(passive_loc);
                return;
            }
            locationResult.gotLocation(null);
        }
    }

    public static String setLocation(Context context, Location location) {
        String mfeatureName = null;
        if (location != null) {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addList = null;// 解析经纬度
            try {
                addList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addList != null && addList.size() > 0) {
                for (int i = 0; i < addList.size(); i++) {
                    Address add = addList.get(i);
                    mfeatureName = add.getFeatureName();
                }
            }
        }
        return mfeatureName;
    }

        public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }

}
