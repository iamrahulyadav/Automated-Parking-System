package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.*;
import android.preference.PreferenceManager;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class SubmitActivity extends Activity implements AdapterView.OnItemSelectedListener{
    protected EditText driverno;
    protected EditText vehicleno;
    protected TextView date;
    protected Button submitButton;
    private Firebase mRef;
    String transpoter_name,vehicle_type;
    private String typeofuser;
    private String email_operator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        typeofuser=preferences.getString("typeofuser",null);
        email_operator=preferences.getString("email",null);
        Button logout=(Button)findViewById(R.id.button_logout);
        driverno= (EditText) findViewById(R.id.editText3);
        vehicleno = (EditText) findViewById(R.id.editText4);
        date= (TextView) findViewById(R.id.textView_date1);
        submitButton= (Button) findViewById(R.id.button_submit);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);
        List<String> vcl_typ = new ArrayList<String>();
        vcl_typ.add("Light");
        vcl_typ.add("Heavy");
        vcl_typ.add("asdhha");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vcl_typ);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Spinner spinner_transporter = (Spinner) findViewById(R.id.spinner3);
        spinner_transporter.setOnItemSelectedListener(this);
        List<String> trnsptr_typ = new ArrayList<String>();
        trnsptr_typ.add("abc");
        trnsptr_typ.add("aadljfhajkdshj");
        trnsptr_typ.add("asdhha");
        // Creating adapter for spinner_transporter
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trnsptr_typ);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner_transporter
        spinner_transporter.setAdapter(dataAdapter1);
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
                String transporter = transpoter_name;
                String drvrno = driverno.getText().toString();
                String vhclno = vehicleno.getText().toString();
                String final_date = date.getText().toString();
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Transporter", transporter);
                graceNickname.put("Driver Number", drvrno);
                graceNickname.put("Vehicle Number", vhclno);
                graceNickname.put("Date", final_date);
                graceNickname.put("Vehicle Type", vehicle_type);
                graceNickname.put("Time of Arrival",localTime);
                graceNickname.put("Cost", 200);
                graceNickname.put("email",email_operator);
                graceNickname.put(typeofuser,"true");
                Firebase newpostref = mRef.child("users").child("data").push();
                newpostref.setValue(graceNickname);
                editor.putString("UserID for data", newpostref.getKey());
                AlertDialog.Builder builder = new AlertDialog.Builder(SubmitActivity.this);
                builder.setMessage("You have successfully uploaded the details!")
                        .setTitle(R.string.submit_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                driverno.setText("");
                vehicleno.setText("");
                Intent intent = new Intent(getApplicationContext(), PrintActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }



    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner3 = (Spinner) parent;
        if(spinner3.getId() == R.id.spinner2)
        {
            vehicle_type=parent.getItemAtPosition(position).toString();
        }
        else if(spinner3.getId() == R.id.spinner3)
        {
            transpoter_name=parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(SubmitActivity.this, TypeofOperator.class);
        startActivity(i);
    }
}
