package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalculateFare extends Activity {
    EditText et_search;
    ImageButton search;
    TextView tv,tv1;
    private String useridold;
    private String localtime;
    private long currenttime;
    private long timetocharge;
    private long globalmillis;
    private int cost=0;
    String postid;
    Firebase mRef;

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
                final String vhlno = et_search.getText().toString();
                Log.d("vehicle", vhlno);

                // Attach an listener to read the data at our posts reference
                Query queryRef = mRef.child("users").child("data").orderByChild("Vehicle Number").equalTo(vhlno);
                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        useridold = snapshot.getKey();
                        TruckDetailsActivity truck = snapshot.getValue(TruckDetailsActivity.class);
                        localtime = truck.gettime();
                        Log.d("asda", localtime);
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
            }
        });
    }

    private void calculate(String localtime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(localtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        globalmillis= date.getTime();
        Log.d("askjdh", String.valueOf(globalmillis));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        currenttime = System.currentTimeMillis();
        timetocharge=currenttime-globalmillis;
        Log.d("timetocharge", String.valueOf(timetocharge));
        calendar.setTimeInMillis(timetocharge);
        String t = sdf.format(calendar.getTime());
        if(timetocharge>=18*60*1000 && timetocharge<24*60*1000){
            cost=cost+75;
        }
        else if(timetocharge>=24*60*1000 && timetocharge<48*60*1000){
            cost=cost+150;
        }else{
            cost=cost+200;
        }
        Log.d("cost", String.valueOf(cost));
        tv1.setText(String.valueOf(cost));
        Map<String, Object> graceNickname = new HashMap<>();
        graceNickname.put("Cost", cost);
        mRef.child("users").child("data").child(postid).updateChildren(graceNickname);
    }
   /* @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(CalculateFare.this, LoginActivity.class);
        startActivity(i);
    }*/
}
