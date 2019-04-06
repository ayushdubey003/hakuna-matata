package in.nitjsrac.pcon.hakunamatata;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class StartActivity extends EasyLocationAppCompatActivity {
    SharedPreferences mSharedPreferences;
    private double lati;
    private double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        final EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        requestSingleLocationFix(easyLocationRequest);
        Toast.makeText(StartActivity.this, "Detecting Location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationPermissionGranted() {
        Toast.makeText(this, "Please wait while current location is fetched....", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionDenied() {
        Toast.makeText(this, "Enable Location for app to function properly", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationReceived(Location location) {
        lati = location.getLatitude();
        longi = location.getLongitude();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lati, longi, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            String address = addresses.get(0).getAddressLine(0);
            mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putFloat("latitude", (float) lati);
            editor.putFloat("longitude", (float) longi);
            editor.putString("address", address);
            editor.commit();
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);
        } else
            Toast.makeText(this, "Location cannot be updated! Please enter location manually", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationProviderEnabled() {
        Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationProviderDisabled() {
        Toast.makeText(this, "Enable Location for app to function properly", Toast.LENGTH_LONG).show();
    }
}
