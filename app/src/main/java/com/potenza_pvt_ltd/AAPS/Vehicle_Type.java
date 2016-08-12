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
import android.widget.ListView;
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

public class Vehicle_Type extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Firebase ref;
    private EditText et1,et3;
    private Spinner spinner;
    private ListView listView;
    private CustomAdapter customAdapter;
    String code_value_1;
    private ArrayList code_value,vehicle_type,tariff;
    final ArrayList<String> code = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;

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
        spinner.setOnItemSelectedListener(this);
        new getSpinnerData(Vehicle_Type.this).execute();

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        new getListViewData(Vehicle_Type.this).execute();

    }
    class getSpinnerData extends AsyncTask<Context,String,String> {
        Activity mActivity;
        public getSpinnerData (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            Query queryRef = ref.child("users").child("Vehicle").child("Vehicle_Code");
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    code.add(String.valueOf(dataSnapshot.getValue()));

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
            Query queryRef1 = ref.child("users").child("Vehicle_Type");
            queryRef1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("val of vehicle type", String.valueOf(dataSnapshot.getValue()));
                    VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                    if(code.contains(post.getCode())){
                        code.remove(post.getCode());
                    }

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
            dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, code);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
        }


    }
    class getListViewData extends AsyncTask<Context,String,String> {
        Activity mActivity;
        public getListViewData (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            Query queryRef = ref.child("users").child("Vehicle_Type");
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                    VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                    Log.d("code",post.getCode());
                    Log.d("vt",post.getVehicle_type());
                    Log.d("fixed",post.getInslip_tariff());
                    code_value.add(post.getCode());
                    vehicle_type.add(post.getVehicle_type());
                    tariff.add(post.getInslip_tariff());
                    customAdapter = new CustomAdapter(getApplication(), vehicle_type,tariff,code_value,3);
                    listView.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    int pos=customAdapter.getPos();
                    code_value.remove(pos);
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


    public void save(View v){
        final String code = code_value_1;
       final String vehicle = et1.getText().toString();
        final String inslip = et3.getText().toString();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("code",code);
        value.put("vehicle_type", vehicle);
        value.put("fixed_tariff", inslip);
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
//        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(Vehicle_Type.this, Masters.class);
        startActivity(i);
    }
}
