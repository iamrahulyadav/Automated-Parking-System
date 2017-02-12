package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Kushagr_Jolly on 6/4/2016.
 */

public class LoginActivity extends Activity {
    SharedPreferences sharedpreferences;
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    String typeofuser;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
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
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("User", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("User", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //ref = new Firebase(Constants.FIREBASE_URL);
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
                    Log.d("Password", password);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Sign In", "signInWithEmail:onComplete:" + task.isSuccessful());
                                    Query query=reference.child("users").orderByChild("email-address");
                                            query.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                            Log.d("key123", snapshot.getKey());
                                            if (snapshot.getKey().contentEquals("Manager") || snapshot.getKey().contentEquals("Operator")) {
                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                    Log.d("VALUE", String.valueOf(snapshot1.getValue()));
                                                    DetailofUser post = snapshot1.getValue(DetailofUser.class);
                                                    Log.d("Email post",post.getEmailaddress());
                                                    String emailvalue = post.getEmailaddress().trim();
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
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w("Sign in Faialed", "signInWithEmail:failed", task.getException());
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("EmailID or Password Incorrect")
                                                .setTitle(R.string.login_error_title)
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                    // ...
                                }
                            });
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void callme(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email",emailAddress);
        editor.putString("typeofuser",typeofuser);
        editor.commit();
    }
}
