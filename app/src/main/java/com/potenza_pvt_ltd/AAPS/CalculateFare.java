package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CalculateFare extends Activity {
    EditText et_search;
    ImageButton search;
    TextView tv,tv1;
    private String useridold;
    private String localtime;
    private int cost=0;
    String postid;
    Firebase mRef;
    int[][] arr;
    private String truck_type;
    private int hours;
    private String vhlno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_fare);
        postid=getIntent().getStringExtra("UniqueID");
        et_search=(EditText)findViewById(R.id.editText_search);
        search=(ImageButton)findViewById(R.id.imageButton);
        tv=(TextView)findViewById(R.id.textView7);
        tv1=(TextView)findViewById(R.id.textView8);
        mRef = new Firebase(Constants.FIREBASE_URL);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vhlno = et_search.getText().toString();
                new getArr(CalculateFare.this).execute();

            }
        });

    }

    private void calculate(String localtime) {

        Date date1=null,date2 = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String current = dateFormat.format(calendar.getTime());
        try {
            date1 = dateFormat.parse(localtime);
            date2 = dateFormat.parse(current);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1!=null && date2!=null) {
            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) (difference / (60 * 60 * 1000));
            int min = (int) (difference/(60 * 1000) % 60);
            int sec= (int) (difference / 1000 % 60);
            hours = (hours < 0 ? -hours : hours);
            Log.i("======= Hours", " :: " + hours + "::" + min);
        }
        for(int i=0;i<arr.length;i++){
                if(hours>=arr[i][0]&& hours<arr[i][1]){
                    Log.d("cost", String.valueOf(arr[i][2]));
                }
        }
        Log.d("cost", String.valueOf(cost));
        tv1.setText(String.valueOf(cost));
        Map<String, Object> graceNickname = new HashMap<>();
        graceNickname.put("Cost", cost);
        mRef.child("users").child("data").child(useridold).updateChildren(graceNickname);
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(CalculateFare.this, TypeofOperator.class);
        startActivity(i);
    }
    class getArr extends AsyncTask<Context,String,String> {
        Activity mActivity;
        public getArr (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            Log.d("vehicle", vhlno);
            // Attach an listener to read the data at our posts reference
            Query queryRef1 = mRef.child("users").child("data").orderByChild("Vehicle Number").equalTo(vhlno);
            queryRef1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    useridold = snapshot.getKey();
                    TruckDetailsActivity truck = snapshot.getValue(TruckDetailsActivity.class);
                    localtime = truck.gettime();
                    truck_type = truck.getVehicleType();
                    Log.d("asda", truck_type);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculateFare.this);
                    builder.setMessage(firebaseError.getMessage())
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            Query queryRef = mRef.child("users").child("Tariff_Details").orderByChild("vehicle_type").equalTo(truck_type);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    TariffDetails truck = snapshot.getValue(TariffDetails.class);
                    arr=truck.getarr();
                    Log.d("arr", Arrays.deepToString(arr));
                    calculate(localtime);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculateFare.this);
                    builder.setMessage(firebaseError.getMessage())
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute(){
        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }

}
