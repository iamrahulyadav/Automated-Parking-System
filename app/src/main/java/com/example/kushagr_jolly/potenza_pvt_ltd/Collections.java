package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
