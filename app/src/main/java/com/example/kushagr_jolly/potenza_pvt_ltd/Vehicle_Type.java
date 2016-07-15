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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vehicle_Type extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Firebase ref;
    private EditText et1,et3;
    private Spinner spinner;
    private ListView listView;
    private CustomAdapter customAdapter;
    String code_value_1;
    private ArrayList code_value,vehicle_type,tariff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__type);
        ref=new Firebase(Constants.FIREBASE_URL);
        et1=(EditText)findViewById(R.id.editText6);
        spinner=(Spinner)findViewById(R.id.spinner4);
        et3=(EditText)findViewById(R.id.editText7);
        code_value=new ArrayList();
        vehicle_type=new ArrayList();
        tariff= new ArrayList();
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
         String[] Letter={"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        final ArrayList<String> code = new ArrayList<String>(Arrays.asList(Letter));
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, code);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        customAdapter = new CustomAdapter(getApplication(), vehicle_type,tariff,code_value);
        listView.setAdapter(customAdapter);
        Query queryRef = ref.child("users").child("Vehicle_Type");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                TariffDetails post = dataSnapshot.getValue(TariffDetails.class);
                code_value.add(post.getCode());
                vehicle_type.add(post.getVehicle_type());
                tariff.add(post.getInslip_tariff());
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

            }
        });
    }

    public void save(View v){
        final String code = code_value_1;
       final String vehicle = et1.getText().toString();
        final String inslip = et3.getText().toString();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("code",code);
        value.put("vehicle_type",vehicle);
        value.put("inslip_tariff",inslip);
        ref.child("users").child("Vehicle_Type").push().setValue(value);
        customAdapter.notifyDataSetChanged();
    }


    public void delete(View v){
        final String []item=customAdapter.getValue();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    code_value_1=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }
}
