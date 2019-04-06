package in.nitjsrac.pcon.hakunamatata;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Script {
    private static DatabaseReference databaseReference;
    SharedPreferences mSharedPreferences;
    public final static double AVERAGE_RADIUS_OF_EARTH_M = 6371000;
    public static int calculateDistanceInMeter(double userLat, double userLng,
                                               double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_M * c));
    }

    public static int doSome(final String restaurant,final double l1, final double l2){

     /*   final double l1=22.1799;
        final double l2=86.1866;*/

        final int[] x = new int[1];
        x[0]=0;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double lati,longi;
                String x1,y;
                x1=  dataSnapshot.child("hotel").child(restaurant).child("lat").getValue().toString();
                y =  dataSnapshot.child("hotel").child(restaurant).child("longi").getValue().toString();

                lati = Double.parseDouble(x1);
                longi = Double.parseDouble(y);

                int dist = calculateDistanceInMeter(l1,l2,lati,longi);

                double l3,l4;
                Character i;
                int f=0;
                long ass;
                for (i ='A';i<='E';i++){
                    ass = (long) dataSnapshot.child("boyz").child(Character.toString(i)).child("assigned").getValue();
                    Log.e("asa",""+ass);
                    if(ass==0){
                        databaseReference.child("boyz").child(Character.toString(i)).child("lat").setValue(l1);
                        databaseReference.child("boyz").child(Character.toString(i)).child("long").setValue(l2);
                        databaseReference.child("boyz").child(Character.toString(i)).child("num").setValue(1);
                        databaseReference.child("boyz").child(Character.toString(i)).child("assigned").setValue(1);
                        x[0] = (int) i - 64;
                        break;
                    }
                    if(ass ==1){
                        String a,b;
                        a=dataSnapshot.child("boyz").child(Character.toString(i)).child("num").getValue().toString();
                        int num = Integer.parseInt(a);
                        if(num==5)
                            continue;
                        a=dataSnapshot.child("boyz").child(Character.toString(i)).child("lat").getValue().toString();
                        b=dataSnapshot.child("boyz").child(Character.toString(i)).child("long").getValue().toString();
                        double c,d;
                        c= Double.parseDouble(a);
                        d= Double.parseDouble(b);

                        double c1,c2;
                        int disto = calculateDistanceInMeter(l1,l2,c,d);
                        if(disto>500)
                            continue;
                        c1=(c*num+l1)/(num+1);
                        c2=(d*num+l2)/(num+1);
                        databaseReference.child("boyz").child(Character.toString(i)).child("lat").setValue(c1);
                        databaseReference.child("boyz").child(Character.toString(i)).child("long").setValue(c2);
                        databaseReference.child("boyz").child(Character.toString(i)).child("num").setValue(num+1);
                        x[0] = (int) i - 64;
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return x[0];
    }
}
