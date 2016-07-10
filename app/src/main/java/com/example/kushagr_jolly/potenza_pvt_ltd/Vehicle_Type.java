package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vehicle_Type extends Activity implements AdapterView.OnItemClickListener {

    private Firebase ref;
    private EditText et1,et3;
    private Spinner spinner;
    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList code,vehicle_type,tariff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__type);
        ref=new Firebase(Constants.FIREBASE_URL);
        et1=(EditText)findViewById(R.id.editText6);
        spinner=(Spinner)findViewById(R.id.spinner4);
        et3=(EditText)findViewById(R.id.editText7);
        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        Query queryRef = ref.child("users").child("Vehicle Type");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                TariffDetails post = dataSnapshot.getValue(TariffDetails.class);
                code.add(post.getCode());
                vehicle_type.add(post.getVehicle_type());
                tariff.add(post.getInslip_tariff());
                customAdapter = new CustomAdapter(getApplication(), code,vehicle_type,tariff);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
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

            }
        });
    }

    public void save(View v){
        final String code = et1.getText().toString();
       // final String vehicle = et2.getText().toString();
        final String inslip = et3.getText().toString();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("code",code);
        //value.put("vehicle_type",vehicle);
        value.put("inslip_tariff",inslip);
        ref.child("users").child("Vehicle_Type").push().setValue(value);
    }
    public void delete(View v){
        final String []item=customAdapter.getValue();
        Query queryRef = ref.child("users").child("Vehicle_Type").orderByChild("vehicle_type").equalTo(item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                ref.child("users").child("Manager").child(snapshot.getKey()).removeValue();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
