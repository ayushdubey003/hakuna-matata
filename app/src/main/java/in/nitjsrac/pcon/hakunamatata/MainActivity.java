package in.nitjsrac.pcon.hakunamatata;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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
        Hotel hotel = new Hotel("","", "", "Hotel Junction");
        ArrayList<Hotel> arrayList = new ArrayList<Hotel>();
        arrayList.add(hotel);
        adapter = new MyAdapter(this, 0, arrayList);
        listView.setAdapter(adapter);

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

        public Hotel(String imageurl, String price, String averagetime, String name){
            this.imageurl = imageurl;
            this.price = price;
            this.averagetime = averagetime;
            this.name = name;
        }

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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.hotel, null, false);

            ((TextView)(convertView.findViewById(R.id.price))).setText("Rs. " + (200 + (int)(Math.random() * 10) * 10));
            ((TextView)convertView.findViewById(R.id.name)).setText(getItem(position).name);
            //((TextView)convertView.findViewById(R.id.averagetime)).setText(getItem(position).averagetime);

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    if(preferences.contains(((TextView)finalConvertView.findViewById(R.id.name)).getText().toString())){
                        Toast.makeText(MainActivity.this, "Order Pending", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean(((TextView)finalConvertView.findViewById(R.id.name)).getText().toString(), true);
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "Processing your Order", Toast.LENGTH_SHORT).show();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Confirm Order ?").setPositiveButton("Yes", dialogListener).
                                setNegativeButton("No", dialogListener).show();
                    }
                }
            });

            return convertView;
        }
    }

}