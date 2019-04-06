package in.nitjsrac.pcon.hakunamatata;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SharedPreferences mSharedPreferences;
    private double lati;
    private double longi;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        LatLng location = new LatLng(mSharedPreferences.getFloat("latitude", (float) 0.0), mSharedPreferences.getFloat("longitude", (float) 0.0));
        mMap.addMarker(new MarkerOptions().position(location).title("Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(location, 8);
        mMap.animateCamera(yourLocation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        TextView et = (TextView) findViewById(R.id.edit);
        String address = mSharedPreferences.getString("address","Random Location");
        et.setText(address);
    }
}
