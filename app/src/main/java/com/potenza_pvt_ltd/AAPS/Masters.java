package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;


public class Masters extends Activity {
String typeofuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masters);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        typeofuser=preferences.getString("typeofuser",null);
    }

    public void manager(View v){
        Intent intent = new Intent(Masters.this, CreateManager.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void Operator(View v){
        Intent intent = new Intent(Masters.this, CreateOperator.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void Vehicle_type(View v){
        Intent intent = new Intent(Masters.this,Vehicle_Type.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void parking_slot(View v){
        Intent intent = new Intent(Masters.this, PSActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void transporter(View v){
        Intent intent = new Intent(Masters.this, Transporter.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void tariff(View v){
        Intent intent = new Intent(Masters.this, TariffActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        if (typeofuser.contentEquals("Manager")) {
            finish(); //finishes the current activity and doesnt save in stock
            Intent i = new Intent(Masters.this, Manager.class);
            startActivity(i);
        }
        else if(typeofuser.contentEquals("Admin")){
            finish(); //finishes the current activity and doesnt save in stock
            Intent i = new Intent(Masters.this, Admin.class);
            startActivity(i);
        }
    }


}
