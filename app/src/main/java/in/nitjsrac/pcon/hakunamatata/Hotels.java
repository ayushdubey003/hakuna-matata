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

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Hotels extends AppCompatActivity {

    private static int RC_SIGN_IN = 99;

    private ArrayAdapter<Hotel> adapter;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);

        listView = findViewById(R.id.list);
        Hotel hotel = new Hotel("", "","", "Hotel");
        ArrayList<Hotel> arrayList = new ArrayList<>();
        //arrayList.add(hotel);
        adapter = new MyAdapter(this, 0, arrayList);
        listView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("hotel");
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            ArrayList<FireBaseHotel> arrayList1 = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try {
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            FireBaseHotel q=ds.getValue(FireBaseHotel.class);
                            arrayList1.add(q);
                            //Toast.makeText(Hotels.this, q.name, Toast.LENGTH_LONG).show();
                        }

                        add(arrayList1);

                    } catch (Exception e){
                        //Toast.makeText(FAQsActivity.this, "Oops! Something went wrong",
                        //      Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add(ArrayList<FireBaseHotel> t){
        adapter.clear();
        for(int i = 0;i < t.size();i ++){
            FireBaseHotel hotel = t.get(i);
            //Log.e("jhbs", hotel.name);
            Hotel k = new Hotel("", "","","");
            k.setName(hotel.name);
            k.lat = hotel.lat;
            k.longi = hotel.longi;
            adapter.add(k);
        }
    }

    private class Hotel{
        String imageurl, price, averagetime, name;
        double lat, longi;

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
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(Hotels.this).inflate(R.layout.hotel, null, false);

            ((TextView)(convertView.findViewById(R.id.price))).setText("Rs. " + (200 + (int)(Math.random() * 10) * 10));
            ((TextView)convertView.findViewById(R.id.name)).setText(getItem(position).name);
            //((TextView)convertView.findViewById(R.id.averagetime)).setText(getItem(position).averagetime);

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    if(preferences.contains(((TextView)finalConvertView.findViewById(R.id.name)).getText().toString())){
                        Toast.makeText(Hotels.this, "Order Pending", Toast.LENGTH_SHORT).show();
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
                                        double lat = getItem(position).lat;
                                        double longi = getItem(position).longi;
                                        //int x = Script.doSome();
                                        //if(x==0){

                                        //}

                                        Toast.makeText(Hotels.this, "Processing your Order", Toast.LENGTH_SHORT).show();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(Hotels.this);
                        builder.setMessage("Confirm Order ?").setPositiveButton("Yes", dialogListener).
                                setNegativeButton("No", dialogListener).show();
                    }
                }
            });

            return convertView;
        }
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
}
