package in.nitjsrac.pcon.hakunamatata;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int RC_SIGN_IN = 99;

    private ArrayAdapter<Hotel> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("hello").setValue("he");

        listView = (ListView)findViewById(R.id.list);

        /*startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(),
                RC_SIGN_IN);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Log.e("str", "mak");
            }
        }
    }

    private class Hotel{
        String imageurl, price, averagetime, name;

        public String getAveragetime() {
            return averagetime;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public void setAveragetime(String averagetime) {
            this.averagetime = averagetime;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    private class MyAdapter extends ArrayAdapter<Hotel>{

        public MyAdapter(@NonNull Context context, int resource, @NonNull List<Hotel> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.hotel, parent);

            ((TextView)(convertView.findViewById(R.id.price))).setText("" + (200 + Math.random() * 100));
            ((TextView)convertView.findViewById(R.id.name)).setText(getItem(position).name);
            ((TextView)convertView.findViewById(R.id.averagetime)).setText(getItem(position).averagetime);

            return convertView;
        }
    }

}