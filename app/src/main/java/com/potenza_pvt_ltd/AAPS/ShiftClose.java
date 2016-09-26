package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.potenza_pvt_ltd.AAPS.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShiftClose extends Activity {
    protected EditText email_operator,email_manager;
    protected EditText password_operator,password_manager;
    protected Button shiftclose;
    Firebase ref;
    String postid;
    String emailAddress_o,emailAddress_m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_close);
        postid=getIntent().getStringExtra("UniqueID");
        Log.d("post1",postid);
        email_operator = (EditText) findViewById(R.id.emailField2);
        password_operator = (EditText) findViewById(R.id.passwordField2);
        email_manager = (EditText) findViewById(R.id.emailField3);
        password_manager = (EditText) findViewById(R.id.passwordField3);
        shiftclose = (Button) findViewById(R.id.shiftClosebutton);
        ref=new Firebase(Constants.FIREBASE_URL);

        shiftclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_o = email_operator.getText().toString();
                String pass_o = password_operator.getText().toString();

                email_o = email_o.trim();
                pass_o = pass_o.trim();

                if (email_o.isEmpty() || pass_o.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShiftClose.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle("Warning!")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    emailAddress_o = email_o;
                    Log.d("Email", email_o);
                    Log.d("Password", pass_o);

                    //Login with an email_o/pass_o combination
                    ref.authWithPassword(email_o, pass_o, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            // Authenticated successfully with payload authData
                            Query queryRef = ref.child("users").child("Operator").orderByChild("email-address").equalTo(emailAddress_o);
                            queryRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                    Log.d("key123", snapshot.getKey());
                                    String email_m = email_manager.getText().toString();
                                    String pass_m = password_manager.getText().toString();
                                    email_m = email_m.trim();
                                    pass_m = pass_m.trim();
                                    if (email_m.isEmpty() || pass_m.isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ShiftClose.this);
                                        builder.setMessage(R.string.login_error_message)
                                                .setTitle("Warning!!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    } else {
                                        emailAddress_m = email_m;
                                        Log.d("Email", email_m);
                                        Log.d("Password", pass_m);

                                        //Login with an email_o/pass_o combination
                                        ref.authWithPassword(email_m, pass_m, new Firebase.AuthResultHandler() {
                                            @Override
                                            public void onAuthenticated(AuthData authData) {
                                                Log.d("abcd", "close time");
                                                // Authenticated successfully with payload authData
                                                Query queryRef = ref.child("users").child("Manager").orderByChild("email-address").equalTo(emailAddress_m);
                                                queryRef.addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                                        Log.d("key123", snapshot.getKey());
                                                        Calendar calendar = Calendar.getInstance();
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        final String localTime = sdf.format(calendar.getTime());
                                                        Map<String, Object> graceNickname = new HashMap<>();
                                                        graceNickname.put("Shift Close Time", localTime);
                                                        ref.child("users").child("timing").child(postid).updateChildren(graceNickname);
                                                        ref.unauth();
                                                        email_operator.setText("");
                                                        password_operator.setText("");
                                                        email_manager.setText("");
                                                        password_manager.setText("");
                                                        Intent intent = new Intent(ShiftClose.this, LoginActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onChildChanged
                                                            (DataSnapshot dataSnapshot, String
                                                                    s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved
                                                            (DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved
                                                            (DataSnapshot dataSnapshot, String
                                                                    s) {

                                                    }

                                                    @Override
                                                    public void onCancelled
                                                            (FirebaseError firebaseError) {

                                                    }
                                                    // ....
                                                });


                                            }

                                            @Override
                                            public void onAuthenticationError(FirebaseError firebaseError) {
                                                // Authenticated failed with error firebaseError
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ShiftClose.this);
                                                builder.setMessage("Please Login Through your credentials")
                                                        .setTitle(R.string.login_error_title)
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                                email_manager.setText("");
                                                email_operator.setText("");
                                                password_manager.setText("");
                                                password_operator.setText("");
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot
                                                                   dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String
                                        s) {

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShiftClose.this);
                            builder.setMessage("Please Login Through your credentials!!!")
                                    .setTitle(R.string.login_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            email_manager.setText("");
                            email_operator.setText("");
                            password_manager.setText("");
                            password_operator.setText("");
                        }
                    });
                }

            }
        });

    }
    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(ShiftClose.this, ShiftOpen.class);
        startActivity(i);
    }
}
