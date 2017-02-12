package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vehicle_Type extends Activity implements AdapterView.OnItemClickListener {

    private FirebaseAuth mAuth;
    DatabaseReference reference;
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
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        et1 = (EditText) findViewById(R.id.editText6);
        et3 = (EditText) findViewById(R.id.editText7);
        vehicle_type = new ArrayList();
        tariff = new ArrayList();
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        customAdapter = new CustomAdapter(getApplication(), vehicle_type, tariff);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        listView.setAdapter(customAdapter);
        Query queryRef = reference.child("users").child("Vehicle_Type");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                vehicle_type.add(post.getVehicle_type());
                tariff.add(post.getFixed_tariff());
                pb.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                customAdapter.notifyDataSetChanged();
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
            public void onCancelled(DatabaseError firebaseError) {

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
        reference.child("users").child("Vehicle_Type").push().setValue(value);
        customAdapter.notifyDataSetChanged();

    }


    public void delete(View v) {
        final String []item = customAdapter.getValue();
        Query queryRef = reference.child("users").child("Vehicle_Type").orderByChild("vehicle_type").equalTo(item[0]);
        Log.d("item",item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("delte func", String.valueOf(item));
                reference.child("users").child("Vehicle_Type").child(snapshot.getKey()).removeValue();
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
            public void onCancelled(DatabaseError firebaseError) {
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
