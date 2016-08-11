package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by Kushagr_Jolly on 6/4/2016.
 */

public class LoginActivity extends Activity {
    SharedPreferences sharedpreferences;
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    String typeofuser;
    Firebase ref;
    String emailAddress;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = (EditText) findViewById(R.id.emailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.shiftopenbutton);

        ref = new Firebase(Constants.FIREBASE_URL);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                email = email.trim();
                password = password.trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    emailAddress = email.trim();
                    Log.d("Email", email);
                    Log.d("Password",password);

                    //Login with an email/password combination
                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Log.d("hello","hello");
                            // Authenticated successfully with payload authData
                            Query queryRef = ref.child("users").orderByChild("email-address");
                            queryRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                    Log.d("key123", snapshot.getKey());
                                    if (snapshot.getKey().contentEquals("Manager") || snapshot.getKey().contentEquals("Operator")) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            Log.d("VALUE", String.valueOf(snapshot1.getValue()));
                                            DetailofUser post = snapshot1.getValue(DetailofUser.class);
                                            String emailvalue = post.getEmail().trim();
                                            if (emailvalue.contentEquals(emailAddress)) {
                                                typeofuser = snapshot.getKey();
                                                if (snapshot.getKey().contentEquals("Operator")) {
                                                    callme();
                                                    Log.d("operator", "function");
                                                    Intent intent = new Intent(LoginActivity.this, ShiftOpen.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                } else if (snapshot.getKey().contentEquals("Manager")) {
                                                    callme();
                                                    Log.d("manager", "function");
                                                    Intent intent = new Intent(LoginActivity.this, Manager.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    } else if (snapshot.getKey().contentEquals("Admin")) {
                                        typeofuser = snapshot.getKey();
                                        callme();
                                        Log.d("admin", "function");
                                        Intent intent = new Intent(LoginActivity.this, Admin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                                // ....
                            });


                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // Authenticated failed with error firebaseError
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(firebaseError.getMessage())
                                    .setTitle(R.string.login_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }

            }
        });

    }
    public void callme(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email",emailAddress);
        editor.putString("typeofuser",typeofuser);
        editor.commit();
    }
}
