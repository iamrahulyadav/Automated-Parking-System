package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class ReportsActivity extends Activity {

    private String typeofuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        typeofuser=preferences.getString("typeofuser",null);
    }
    public void summary(View v){
        Intent intent = new Intent(ReportsActivity.this, SummaryReports.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void inslip(View v){
        Intent intent = new Intent(ReportsActivity.this, InslipReports.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void outslip(View v){
        Intent intent = new Intent(ReportsActivity.this, OutslipReports.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void slot(View v){
        Intent intent = new Intent(ReportsActivity.this, SlotReport.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        if (typeofuser.contentEquals("Manager")) {
            finish(); //finishes the current activity and doesnt save in stock
            Intent i = new Intent(ReportsActivity.this, Manager.class);
            startActivity(i);
        }
        else if(typeofuser.contentEquals("Admin")){
            finish(); //finishes the current activity and doesnt save in stock
            Intent i = new Intent(ReportsActivity.this, Admin.class);
            startActivity(i);
        }
    }
}
