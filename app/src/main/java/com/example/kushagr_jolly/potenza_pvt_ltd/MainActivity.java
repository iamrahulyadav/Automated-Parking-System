package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class MainActivity extends Activity {

    private Button user_loginButton;
    private Button manager_loginButton;
    private Button admin_loginButton;
    String typeofuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_loginButton= (Button) findViewById(R.id.button_user);
        manager_loginButton= (Button) findViewById(R.id.button_manager);
        admin_loginButton= (Button) findViewById(R.id.button_admin);

        final Firebase ref = new Firebase(Constants.FIREBASE_URL);

        user_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeofuser= (String) ((Button) v).getText();
                Log.d("Typeofuser",typeofuser);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("typeofuser", typeofuser);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        manager_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeofuser= (String) ((Button) v).getText();
                Log.d("Typeofuser",typeofuser);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("typeofuser", typeofuser);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        admin_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeofuser= (String) ((Button) v).getText();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("typeofuser", typeofuser);
                intent.getBundleExtra(typeofuser);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }
}
