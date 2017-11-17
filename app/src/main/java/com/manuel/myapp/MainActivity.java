package com.manuel.myapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.gson.Gson;
import com.manuel.myapp.model.Data;
import com.manuel.myapp.tools.PermissionUtils;
import com.manuel.myapp.tools.SingletonVolley;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionChecker.PermissionResult {



    String URL = "https://api.darksky.net/forecast/00605c1779f8bfb41a24c94a48dc8a64/";
    String URL_PARAM = "?units=si";
    MainActivity thisAtivity = this;
    GoogleApiClient mGoogleApiClient;

    TextView tvAir;
    TextView tvCity;
    TextView tvWeather;
    TextView tvPrecipitation;

    TextView tvOneDayMax;
    TextView tvOneDayMin;
    TextView tvTwoDayMax;
    TextView tvTwoDayMin;
    TextView tvThreeDayMax;
    TextView tvThreeDayMin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();


        //permissionCheck();
        accessingGoogleAPI();
        updateLocation();

        //getData();
    }

    private void initialize() {
        tvAir = (TextView) findViewById(R.id.tvAir);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvWeather = (TextView) findViewById(R.id.tvWeather);
        tvPrecipitation = (TextView) findViewById(R.id.tvPrecipitation);

        tvOneDayMax = (TextView)findViewById(R.id.tvOneDayMax);
        tvOneDayMin = (TextView)findViewById(R.id.tvOneDayMin);
        tvTwoDayMax = (TextView)findViewById(R.id.tvTwoDayMax);
        tvTwoDayMin = (TextView)findViewById(R.id.tvTwoDayMin);
        tvThreeDayMax = (TextView)findViewById(R.id.tvThreeDayMax);
        tvThreeDayMin = (TextView)findViewById(R.id.tvThreeDayMin);
    }

    private void getData(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Data data = new Gson().fromJson(response.toString(), Data.class);

                        setValues(data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("", "Error Respuesta en JSON: " + error.getMessage());

                    }
                });

        SingletonVolley.getInstance(thisAtivity).addToRequestQueue(jsonObjectRequest);
    }

    private void permissionCheck() {
        ArrayList<String> permissions = new ArrayList<>();
        PermissionUtils permissionUtils;

        permissionUtils = new PermissionUtils(this);

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);
    }

    private void accessingGoogleAPI() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    private void updateLocation() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

    }


    private void setValues(Data data) {
        tvCity.setText(data.getTimezone());
        tvAir.setText(data.getCurrently().getWindSpeed() + "m/s");
        tvWeather.setText(data.getCurrently().getTemperature());
        tvPrecipitation.setText(data.getCurrently().getPrecipProbability() + "%");

        tvOneDayMax.setText(data.getDaily().getData().get(0).getTemperatureMax());
        tvOneDayMin.setText(data.getDaily().getData().get(0).getTemperatureMin());

        tvTwoDayMax.setText(data.getDaily().getData().get(1).getTemperatureMax());
        tvTwoDayMin.setText(data.getDaily().getData().get(1).getTemperatureMin());

        tvThreeDayMax.setText(data.getDaily().getData().get(2).getTemperatureMax());
        tvThreeDayMin.setText(data.getDaily().getData().get(2).getTemperatureMin());
    }


    //----------------------------------------------------------------------------------------------
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String x = "";

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(thisAtivity,"Por favor activar permisos de ubicacion",Toast.LENGTH_LONG).show();
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {


            getData(URL+String.valueOf(mLastLocation.getLatitude())+","+String.valueOf(mLastLocation.getLongitude())+URL_PARAM);
            String xs = "";
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
