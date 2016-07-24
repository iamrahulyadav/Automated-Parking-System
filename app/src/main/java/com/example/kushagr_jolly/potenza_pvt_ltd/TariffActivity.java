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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Map;

public class TariffActivity extends Activity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    Firebase ref;
    EditText et1,et2,et3,et4;
    Spinner spinner;
    private String code_value_1;
    private ListView listView;
    private CustomAdapter customAdapter;
    final ArrayList<String> vehicle_type= new ArrayList<String>();
    final ArrayList<String> tariff = new ArrayList<String>();
    final ArrayList<String> code_value= new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariff);
        ref=new Firebase(Constants.FIREBASE_URL);
        et1=(EditText)findViewById(R.id.editText15);
        et2=(EditText)findViewById(R.id.editText16);
        et3=(EditText)findViewById(R.id.editText17);
        et4=(EditText)findViewById(R.id.editText18);
        spinner=(Spinner)findViewById(R.id.spinner5);
        listView=(ListView)findViewById(R.id.listView);
        spinner.setOnItemSelectedListener(this);
        final ArrayList<String> code = new ArrayList<String>();
        Query queryRef = ref.child("users").child("Vehicle_Type");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                code.add(post.getVehicle_type());

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, code);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        customAdapter = new CustomAdapter(getApplication(), vehicle_type,tariff,code_value,1);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        Query queryRef1 = ref.child("users").child("Vehicle_Type");
        queryRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", dataSnapshot.getKey());
                TariffDetails post = dataSnapshot.getValue(TariffDetails.class);
                Log.d("hell", post.getVehicle_type());
                Log.d("hell", post.getTotal_slab_hrs());
                Log.d("hell", post.getTariff());
                code_value.add(post.getVehicle_type());
                vehicle_type.add(post.getTotal_slab_hrs());
                tariff.add(post.getTariff());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    code_value_1=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void save(View v){
        final String vehicle_type = code_value_1;
        final String total_slab = et1.getText().toString();
        final String no_of_slab = et2.getText().toString();
        final String inc_dur_hrs = et3.getText().toString();
        final String tariff = et4.getText().toString();

        Map<String, Object> value = new HashMap<String, Object>();
        value.put("vehicle_type",vehicle_type);
        value.put("total_slab_hrs",total_slab);
        value.put("no_of_slab_hrs", no_of_slab);
        value.put("inc_dur_hrs", inc_dur_hrs);
        value.put("inslip-tariff", tariff);

        ref.child("users").child("Tariff_Details").push().setValue(value);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TariffActivity.this);
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
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(TariffActivity.this, Masters.class);
        startActivity(i);
    }
}
