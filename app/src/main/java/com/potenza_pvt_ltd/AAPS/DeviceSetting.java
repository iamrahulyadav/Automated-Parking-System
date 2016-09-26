package com.potenza_pvt_ltd.AAPS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class DeviceSetting extends AppCompatActivity {
    Firebase ref= new Firebase(Constants.FIREBASE_URL);
    EditText editText1,editText2,editText3,editText4,editText5,editText6,editText7;
    String header1,header2,header3,header4,header5,footer1,footer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        editText1=(EditText)findViewById(R.id.editText19);
        editText2=(EditText)findViewById(R.id.editText20);
        editText3=(EditText)findViewById(R.id.editText21);
        editText4=(EditText)findViewById(R.id.editText22);
        editText5=(EditText)findViewById(R.id.editText23);
        editText6=(EditText)findViewById(R.id.editText24);
        editText7=(EditText)findViewById(R.id.editText25);
    }

    public void save(View v){
        header1=editText1.getText().toString();
        header2=editText2.getText().toString();
        header3=editText3.getText().toString();
        header4=editText4.getText().toString();
        header5=editText5.getText().toString();
        footer1=editText6.getText().toString();
        footer2=editText7.getText().toString();

        Map<String, Object> value = new HashMap<String, Object>();
        value.put("header1", header1);
        value.put("header2", header2);
        value.put("header3", header3);
        value.put("header4", header4);
        value.put("header5", header5);
        value.put("footer1", footer1);
        value.put("footer2", footer2);

        ref.child("users").child("Slip_Details").updateChildren(value);

    }
    @Override
    public void onBackPressed()
    {
            finish(); //finishes the current activity and doesnt save in stock
            Intent i = new Intent(DeviceSetting.this, Admin.class);
            startActivity(i);
        }
}
