package linkon.siddique.smartprofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private int permissionId = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView, disTextView;
    static double curLatitude, curLongitude;
    String latText, lonText, disText;
    private AudioManager myAudioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MyService.class);
        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        disTextView = findViewById(R.id.disTextView);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        getLastLocation();

        //curLatitude = Double.parseDouble(latTextView.getText().toString());
        //curLongitude = Double.parseDouble(lonTextView.getText().toString());

        if (getDistance(this.curLatitude, this.curLongitude, 23.777176, 90.399452) > 0.01) {
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(MainActivity.this, "Now in normal mode", Toast.LENGTH_SHORT).show();
        } else {
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            //myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Toast.makeText(MainActivity.this, "Now in silent mode", Toast.LENGTH_SHORT).show();
        }

        //Toast.makeText(MainActivity.this, curLatitude+"", Toast.LENGTH_LONG).show();
        //Toast.makeText(MainActivity.this, curLongitude+"", Toast.LENGTH_LONG).show();
        ///////////////////////////
        /*
        Button high = (Button) findViewById(R.id.button5);
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(MainActivity.this, "Now in normal mode", Toast.LENGTH_SHORT).show();
            }
        });

        Button silent = (Button) findViewById(R.id.button6);
        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(MainActivity.this, "Now in silent mode", Toast.LENGTH_SHORT).show();
            }
        });
         */
        //////////////////////////////////
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            getLastLocation();
        }
    }

    public void onClickStart(View view) {
        startService(intent);
    }

    public void onClickStop(View view) {
        stopService(intent);
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                permissionId
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionId) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            requestNewLocationData();
                            /*if (location == null) {
                                requestNewLocationData();
                            } else {
                                latTextView.setText(location.getLatitude()+"");
                                lonTextView.setText(location.getLongitude()+"");
                            }*/
                        }
                    }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent2);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        //mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latText = String.format("%.6f", mLastLocation.getLatitude());
            lonText = String.format("%.6f", mLastLocation.getLongitude());

            latTextView.setText(latText);
            lonTextView.setText(lonText);

            curLatitude = Double.parseDouble(latTextView.getText().toString());
            curLongitude = Double.parseDouble(lonTextView.getText().toString());
        }
    };

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; //M = 3958.75 miles, km = 6371, m = 6371e3, cm = 637100000, if < 0.02 km
    /*
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon2);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLon / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;
*/
/*
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515 * 1.609344;

*/
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        disText = String.format("%.6f", dist);
        disTextView.setText(disText);

        return Double.parseDouble(disText);
    }
}