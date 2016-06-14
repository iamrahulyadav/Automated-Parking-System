package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kushagr_Jolly on 6/4/2016.
 */

public class LoginActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    String typeofuser;
    int[] arr = new int[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        typeofuser = bundle.getString("typeofuser");
        emailEditText = (EditText) findViewById(R.id.emailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);

        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
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
                    final String emailAddress = email;
                    Log.d("Email", email);
                    Log.d("Password",password);

                    //Login with an email/password combination
                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            // Authenticated successfully with payload authData
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("email", emailAddress);
                            map.put(typeofuser, "true");

                            Firebase newpostref = ref.child("users").push();

                            newpostref.setValue(map);
                            String postId = newpostref.getKey();
                            Log.d("userid",postId);
                            Log.d("type",typeofuser);
                            if(typeofuser.contentEquals("User")) {
                                Intent intent = new Intent(LoginActivity.this, TypeofOperator.class);
                                intent.putExtra("UniqueID", postId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if (typeofuser.contentEquals("Manager")){
                                Intent intent = new Intent(LoginActivity.this, Manager.class);
                                intent.putExtra("UniqueID", postId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if (typeofuser.contentEquals("Admin")){
                                Intent intent = new Intent(LoginActivity.this, Admin.class);
                                intent.putExtra("UniqueID", postId);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

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
}
