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
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TariffActivity extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{
    Firebase ref;
    EditText et1,et2,et3,et4;
    Spinner spinner;
    private String code_value_1="";
    final ArrayList<String> vehicle_type= new ArrayList<String>();
    final ArrayList<String> flag= new ArrayList<String>();
    private String flag_code;
    ArrayAdapter<String> dataAdapter;
    final ArrayList<String> code = new ArrayList<String>();
    int [][] tar_arr;
    final ArrayList<Integer> arr= new ArrayList<Integer>();
    CustomGrid adapter;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariff);
        ref=new Firebase(Constants.FIREBASE_URL);
        et1=(EditText)findViewById(R.id.editText15);
        et2=(EditText)findViewById(R.id.editText16);
        et3=(EditText)findViewById(R.id.editText17);
        et4=(EditText)findViewById(R.id.editText18);
        spinner=(Spinner)findViewById(R.id.spinner10);
        spinner.setOnItemSelectedListener(this);
        new getSpinnerData(TariffActivity.this).execute();
        gridView=(GridView)findViewById(R.id.grid);
        adapter = new CustomGrid(getApplicationContext(), tar_arr,flag_code);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(parent.getContext(), "You Clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dataAdapter.notifyDataSetChanged();
        code_value_1=parent.getItemAtPosition(position).toString();
        Log.d("code_value_1", code_value_1);
        flag_code=flag.get(position);
        new getListviewdata(TariffActivity.this).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        dataAdapter.notifyDataSetChanged();
    }

    public void save(View v){
        final String vehicle_type = code_value_1;
        Log.d("vehicle-type",code_value_1);
        final String total_slab = et1.getText().toString();
        final String no_of_slab = et2.getText().toString();
        final String inc_dur_hrs = et3.getText().toString();
        final String tariff = et4.getText().toString();
        int max=Integer.valueOf(total_slab)/Integer.valueOf(no_of_slab);//slab size of each
        int[][] arr = new int[Integer.valueOf(no_of_slab)][3];
        int inc=0;
        for(int j=0;j<Integer.valueOf(no_of_slab);j++){
            arr[j][1]=max+inc;
            arr[j][2]= Integer.parseInt(tariff);
            if (inc > 0) {
                arr[j][0]=0+inc+1;
            }
            else{
                arr[j][0]=0+inc;
            }
            inc=inc+max;
        }
        Log.d("this is my array", "arr: " + Arrays.deepToString(arr));
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("vehicle_type",vehicle_type);
        value.put("arr",arr);
        value.put("total_slab_hrs",total_slab);
        value.put("no_of_slab_hrs", no_of_slab);
        value.put("inc_dur_hrs", inc_dur_hrs);
        value.put("inslip_tariff", tariff);
        ref.child("users").child("Tariff_Details").push().setValue(value);
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
    }
    public void delete(View v){
        Query queryRef = ref.child("users").child("Tariff_Details").orderByChild("vehicle_type").equalTo(flag_code);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                ref.child("users").child("Tariff_Details").child(snapshot.getKey()).removeValue();
                adapter.notifyDataSetChanged();
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
        Intent i = new Intent(TariffActivity.this, Masters.class);
        startActivity(i);
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
            Query queryRef = ref.child("users").child("Vehicle_Type");
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                    VehicleTypeDetails post = dataSnapshot.getValue(VehicleTypeDetails.class);
                    code.add(post.getVehicle_type());
                    flag.add(post.getCode());
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

    class getListviewdata extends AsyncTask<Context,String,String> {
        Activity mActivity;
        public getListviewdata (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            Log.d("Listview in tariff","taridd");
            if(code_value_1.isEmpty()==false) {
                Log.d("adsasd", code_value_1);
                Query queryRef1 = ref.child("users").child("Tariff_Details").orderByChild("vehicle_type").equalTo(code_value_1);
                queryRef1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("value of get data", dataSnapshot.getKey());
                        TariffDetails post = dataSnapshot.getValue(TariffDetails.class);
                        tar_arr = post.getarr();
                        for (int i = 0; i < tar_arr.length; i++) {
                            for (int j = 0; j < 3; j++) {
                                Log.d("ARR", String.valueOf(tar_arr[i][j]));
                                arr.add(tar_arr[i][j]);
                            }
                        }
                        adapter = new CustomGrid(getApplicationContext(), tar_arr,flag_code);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        tar_arr = null;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
            adapter.notifyDataSetChanged();
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
            if(tar_arr!=null){
                adapter = new CustomGrid(getApplicationContext(), tar_arr,flag_code);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(parent.getContext(), "You Clicked at " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                adapter.notifyDataSetChanged();
            }
        }

    }
}

