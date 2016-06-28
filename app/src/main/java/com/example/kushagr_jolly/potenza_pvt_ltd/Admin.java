package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {

    protected Button addentry,view;
    private String typeofuser;
    String muserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Bundle bundle = getIntent().getExtras();
        typeofuser = bundle.getString("typeofuser");
        muserid=getIntent().getStringExtra("UniqueID");
        addentry=(Button)findViewById(R.id.button_addent_admin);
        view=(Button)findViewById(R.id.button_view_admin);


        addentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, LoginActivity.class);
                intent.putExtra("typeofuser", typeofuser);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, CardViewActivity.class);
                intent.putExtra("typeofuser", typeofuser);
                intent.putExtra("UniqueID", muserid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
