package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftOpen extends Activity implements AdapterView.OnItemSelectedListener {
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button shiftopen;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressBar pb;
    String emailAddress;
    protected String aps;
    String postid,emailAddress_operator;
    private String typeofuser;
    SharedPreferences.Editor editor;
    String localTime;
    private ArrayAdapter<String> dataAdapter;
    List<String> slotnum = new ArrayList<String>();
    Spinner spinner;
    private LinearLayout linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_open);
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
        emailAddress_operator = preferences.getString("email", null);
        typeofuser=preferences.getString("typeofuser", null);
        emailEditText = (EditText) findViewById(R.id.emailFieldso);
        passwordEditText = (EditText) findViewById(R.id.passwordFieldso);
        shiftopen = (Button) findViewById(R.id.shiftopenbutton);
        spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        Query queryRef = reference.child("users").child("Slot_Name");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                slotnum.add(String.valueOf(dataSnapshot.getValue()));
                dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, slotnum);
                dataAdapter.notifyDataSetChanged();
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                pb.setVisibility(View.GONE);
                linear_layout.setVisibility(View.VISIBLE);
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
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        //new getSpinnerData(ShiftOpen.this).execute();
        dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, slotnum);
        dataAdapter.notifyDataSetChanged();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        localTime = sdf.format(calendar.getTime());
        if(localTime.contains("p.m.")){
            Log.d("localtime",localTime);
            localTime=localTime.replace("p.m.","PM");
        }
        else if(localTime.contains("pm")){
            Log.d("localtime",localTime);
            localTime=localTime.replace("pm","PM");
        }
        else if(localTime.contains("am")){
            Log.d("localtime",localTime);
            localTime=localTime.replace("am","AM");
        }
        else if(localTime.contains("a.m.")){
            Log.d("localtime",localTime);
            localTime=localTime.replace("a.m.","AM");
        }
        shiftopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                email = email.trim();
                password = password.trim();

                if (email.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShiftOpen.this);
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
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ShiftOpen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Authenticated successfully with payload authData
                                    Query queryRef = reference.child("users").orderByChild("emailaddress");
                                    queryRef.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                            Log.d("key123", snapshot.getKey());
                                            if (snapshot.getKey().contentEquals("Manager")) {
                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                    DetailofUser post = snapshot1.getValue(DetailofUser.class);
                                                    String emailvalue = post.getEmailaddress().trim();
                                                    if (emailvalue.contentEquals(emailAddress)) {
                                                        callme();
                                                        editor.putString("aps", aps);
                                                        Intent intent = new Intent(ShiftOpen.this, TypeofOperator.class);
                                                        intent.putExtra("UniqueID", postid);
                                                        intent.putExtra("aps", aps);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
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
                                        public void onCancelled(DatabaseError firebaseError) {

                                        }
                                        // ....
                                    });
                                    if (!task.isSuccessful()) {
                                        Log.w("Sign in Faialed", "signInWithEmail:failed", task.getException());
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ShiftOpen.this);
                                        builder.setMessage("EmailID or Password Incorrect")
                                                .setTitle(R.string.login_error_title)
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
            }
        });
        
    }

    private void callme() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", emailAddress_operator);
        map.put(typeofuser, "true");
        map.put("Shift Open Time", localTime);
        map.put("aps", aps);
        DatabaseReference newpostref=reference.child("users").child("timing").push();
        newpostref.setValue(map);
        postid=newpostref.getKey();
        editor.putString("PostID for timing", postid);
        editor.putString("aps",aps);
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        aps= parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(ShiftOpen.this, LoginActivity.class);
        startActivity(i);
    }

}
