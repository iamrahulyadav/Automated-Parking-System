package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubmitActivity extends AppCompatActivity {
    protected EditText contractorname;
    protected EditText drivername;
    protected EditText driverno;
    protected EditText vehicleno;
    protected TextView date;
    protected Button submitButton;
    private Firebase mRef;
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        mUserId=getIntent().getStringExtra("UniqueID");
        Log.d("xyz",mUserId);
        contractorname= (EditText) findViewById(R.id.editText);
        drivername= (EditText) findViewById(R.id.editText2);
        driverno= (EditText) findViewById(R.id.editText3);
        vehicleno = (EditText) findViewById(R.id.editText4);
        date= (TextView) findViewById(R.id.textView_date1);
        submitButton= (Button) findViewById(R.id.button_submit);
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        date.setText(new StringBuilder().append(dd).append("/").append(mm+1).append("/").append(yy));
                // Month is 0 based, just add 1
                //.append(yy).append("-").append(mm + 1).append("-")
                //.append(dd));

        // Check Authentication
        mRef = new Firebase(Constants.FIREBASE_URL);
        if (mRef.getAuth() == null) {
            loadLoginView();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String contrnm = contractorname.getText().toString();
                String drvrnm = drivername.getText().toString();
                String drvrno = driverno.getText().toString();
                String vhclno = vehicleno.getText().toString();
                String final_date = date.getText().toString();

                /*TruckDetailsActivity truck = new TruckDetailsActivity();
                truck.setContractorname(contrnm);
                truck.setDrivername(drvrnm);
                truck.setDriverno(drvrno);
                truck.setVehicleno(vhclno);
                truck.setDate(final_date);
                truck.setAPS("free");*/
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Contractor Name", contrnm);
                graceNickname.put("Driver Name", drvrnm);
                graceNickname.put("Driver Number", drvrno);
                graceNickname.put("Vehicle Number", vhclno);
                graceNickname.put("Date", final_date);
                graceNickname.put("aps", "free");


                mRef.child("users").child(mUserId).updateChildren(graceNickname);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_truck_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
