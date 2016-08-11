package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Collections extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(Collections.this, TypeofOperator.class);
        startActivity(i);
    }
}
