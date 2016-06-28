package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kushagr_Jolly on 6/4/2016.
 */

public class LoginActivity extends Activity {
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton,signupButton;
    String typeofuser;
    String postId;
    Firebase ref;
    String emailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        typeofuser = bundle.getString("typeofuser");
        emailEditText = (EditText) findViewById(R.id.emailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton=(Button)findViewById(R.id.signupbutton);
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
                    emailAddress = email;
                    Log.d("Email", email);
                    Log.d("Password",password);

                    //Login with an email/password combination
                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {


                            // Authenticated successfully with payload authData
                            Query queryRef = ref.child("users").orderByChild("emailID").equalTo(emailAddress);
                            queryRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                    Log.d("key123",snapshot.getKey());
                                    Log.d("value", String.valueOf(typeofuser.contentEquals(snapshot.getKey())));
                                    if(typeofuser.contentEquals(snapshot.getKey())) {
                                        if(snapshot.getKey().contentEquals("Operator") ){
                                            callme();
                                            Log.d("operator", "function");
                                            Intent intent = new Intent(LoginActivity.this, TypeofOperator.class);
                                            intent.putExtra("UniqueID", postId);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else if (snapshot.getKey().contentEquals("Manager")){
                                            callme();
                                            Log.d("manager", "function");
                                            Intent intent = new Intent(LoginActivity.this, Manager.class);
                                            intent.putExtra("typeofuser",typeofuser);
                                            intent.putExtra("UniqueID", postId);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else if (snapshot.getKey().contentEquals("Admin")){
                                            callme();
                                            Log.d("admin", "function");
                                            Intent intent = new Intent(LoginActivity.this, Admin.class);
                                            intent.putExtra("typeofuser",typeofuser);
                                            intent.putExtra("UniqueID", postId);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("Please Login through your section")
                                                .setTitle(R.string.login_error_title)
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        emailEditText.setText("");
                                        passwordEditText.setText("");
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
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("typeofuser",typeofuser);
                intent.putExtra("UniqueID", postId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
    public void callme(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", emailAddress);
        map.put(typeofuser, "true");
        Firebase newpostref = ref.child("users").child("data").push();
        newpostref.setValue(map);
        postId = newpostref.getKey();
    }
}
