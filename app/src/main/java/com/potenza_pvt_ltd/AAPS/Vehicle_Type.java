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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.potenza_pvt_ltd.AAPS.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Vehicle_Type extends Activity implements AdapterView.OnItemClickListener {

    Firebase ref;
    private EditText et1, et3;
    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList  vehicle_type, tariff;
    final ArrayList<String> code = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    ProgressBar pb;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__type);
        ref = new Firebase(Constants.FIREBASE_URL);
        et1 = (EditText) findViewById(R.id.editText6);
        et3 = (EditText) findViewById(R.id.editText7);
        vehicle_type = new ArrayList();
        tariff = new ArrayList();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        Query queryRef = ref.child("users").child("Vehicle_Type");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                Log.d("vt", post.getVehicle_type());
                Log.d("fixed", post.getInslip_tariff());
                vehicle_type.add(post.getVehicle_type());
                tariff.add(post.getInslip_tariff());
                customAdapter = new CustomAdapter(getApplication(), vehicle_type, tariff, 3);
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int pos = customAdapter.getPos();
                vehicle_type.remove(pos);
                tariff.remove(pos);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public void save(View v) {
        final String vehicle = et1.getText().toString();
        final String inslip = et3.getText().toString();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("vehicle_type", vehicle);
        value.put("fixed_tariff", inslip);
        ref.child("users").child("Vehicle_Type").push().setValue(value);
        customAdapter.notifyDataSetChanged();
    }


    public void delete(View v) {
        final String[] item = customAdapter.getValue();
        Query queryRef = ref.child("users").child("Vehicle_Type").orderByChild("vehicle_type").equalTo(item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("delte func", String.valueOf(item));
                ref.child("users").child("Vehicle_Type").child(snapshot.getKey()).removeValue();
                customAdapter.notifyDataSetChanged();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Vehicle_Type.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(Vehicle_Type.this, Masters.class);
        startActivity(i);
    }
}
