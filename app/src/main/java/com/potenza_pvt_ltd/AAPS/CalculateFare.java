package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateFare extends Activity {
    List<ArrayList<String>> tararr;
    int [][] tar_arr;
    private String[][] tar_arr1;
    EditText et_search;
    ImageButton search;
    TextView tv,tv1;
    private String useridold;
    private String localtime;
    private int cost=0;
    String postid;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    int[][] arr;
    private String truck_type;
    private int hours;
    private String vhlno;
    private String truck_date;
    private String local_datetime;
    ProgressBar pb,pb1;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_fare);
        postid=getIntent().getStringExtra("UniqueID");
        et_search=(EditText)findViewById(R.id.editText_search);
        search=(ImageButton)findViewById(R.id.imageButton);
        tv=(TextView)findViewById(R.id.textView7);
        tv1=(TextView)findViewById(R.id.textView8);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb1=(ProgressBar)findViewById(R.id.progressBar1);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("User", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("User", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                pb1.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                vhlno = et_search.getText().toString();
                Log.d("vehicle", vhlno);
                // Attach an listener to read the data at our posts reference
                Query queryRef1 = reference.child("users").child("data").orderByChild("Vehicle Number").equalTo(vhlno);
                queryRef1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        useridold = snapshot.getKey();
                        TruckDetailsActivity truck = snapshot.getValue(TruckDetailsActivity.class);
                        truck_date = truck.getDate();
                        localtime = truck.getToa();
                        truck_type = truck.getVtype();
                        local_datetime = truck_date + " " + localtime;
                        Log.d("asda", truck_type);
                        pb.setVisibility(View.GONE);
                        Array();
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
                    public void onCancelled(DatabaseError firebaseError) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalculateFare.this);
                        builder.setMessage(firebaseError.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                if(useridold==null){
                    pb.setVisibility(View.GONE);
                    pb1.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void Array() {

        Query queryRef = reference.child("users").child("Tariff_Details").orderByChild("vtype").equalTo(truck_type);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                TariffDetails truck = snapshot.getValue(TariffDetails.class);
                tararr=truck.getArr();
                Log.d("tararr",tararr.toString());
                tar_arr1= new String[tararr.size()][];
                for (int i = 0; i < tararr.size(); i++) {
                    ArrayList<String> row = tararr.get(i);
                    tar_arr1[i] = row.toArray(new String[row.size()]);
                    Log.d("tararr1", String.valueOf(tar_arr1.length));
                }
                arr=new int[tar_arr1.length][3];
                for(int i=0;i<tar_arr1.length;i++){
                    for(int j=0;j<3;j++){
                        Log.d("i,j", String.valueOf(Integer.parseInt(tar_arr1[i][j])));
                        arr[i][j]=Integer.parseInt(tar_arr1[i][j]);
                    }
                }
                Log.d("arr", Arrays.deepToString(arr));
                pb1.setVisibility(View.GONE);
                calculate(local_datetime);

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
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    private void calculate(String localtime) {

        Date date1=null,date2 = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
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
                    cost=arr[i][2];
                }
        }
        Log.d("cost", String.valueOf(cost));
        tv1.setText(String.valueOf(cost));
        Map<String, Object> graceNickname = new HashMap<>();
        graceNickname.put("Cost", String.valueOf(cost));
        reference.child("users").child("data").child(useridold).updateChildren(graceNickname);
        if(pb.getVisibility()==View.GONE && pb1.getVisibility()==View.GONE){
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(CalculateFare.this, TypeofOperator.class);
        startActivity(i);
    }


}
