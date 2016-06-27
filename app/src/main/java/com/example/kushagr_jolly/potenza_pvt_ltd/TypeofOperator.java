package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeofOperator extends AppCompatActivity {

    private Button maingate,slotgate;
    protected Button calculateButton;
    private long currenttime;
    private long timetocharge;
    private long globalmillis;
    private int cost=0;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeof_operator);
        final String postid=getIntent().getStringExtra("UniqueID");
        maingate=(Button)findViewById(R.id.button_mainoperator);
        slotgate=(Button)findViewById(R.id.button_slotoperator);
        calculateButton=(Button)findViewById(R.id.button_calculate_fare);

        maingate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypeofOperator.this,SubmitActivity.class);
                intent.putExtra("UniqueID",postid);
                startActivity(intent);

            }
        });
        mRef=new Firebase(Constants.FIREBASE_URL);
        slotgate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypeofOperator.this,ParkingSlot.class);
                intent.putExtra("UniqueID",postid);
                startActivity(intent);
            }
        });/*
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = null; // You will need try/catch around this
        try {
            //date = formatter.parse(localtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        globalmillis= date.getTime();
        Log.d("askjdh", String.valueOf(globalmillis));

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                currenttime = System.currentTimeMillis();
                timetocharge=currenttime-globalmillis;
                calendar.setTimeInMillis(timetocharge);
                String t = sdf.format(calendar.getTime());
                if(timetocharge>=18*60*1000 && timetocharge<24*60*1000){
                    cost+=75;
                }
                else if(timetocharge>=24*60*1000 && timetocharge<48*60*1000){
                    cost+=150;
                }
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Cost", cost);
                mRef.child("users").child("data").child(postid).updateChildren(graceNickname);
            }
        });*/
    }

}
