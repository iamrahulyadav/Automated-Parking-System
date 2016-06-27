package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


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

        date.setText(new StringBuilder().append(dd).append("/").append(mm+1).append("/").append(yy));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String localTime = sdf.format(calendar.getTime());
        Log.d("locaoltime",localTime);
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
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Contractor Name", contrnm);
                graceNickname.put("Driver Name", drvrnm);
                graceNickname.put("Driver Number", drvrno);
                graceNickname.put("Vehicle Number", vhclno);
                graceNickname.put("Date", final_date);
                graceNickname.put("aps", "free");
                graceNickname.put("Time",localTime);
                mRef.child("users").child("data").child(mUserId).updateChildren(graceNickname);
                AlertDialog.Builder builder = new AlertDialog.Builder(SubmitActivity.this);
                builder.setMessage("You have successfully uploaded the details!")
                        .setTitle(R.string.submit_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }



    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
