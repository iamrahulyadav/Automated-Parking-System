package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftOpen extends Activity implements AdapterView.OnItemSelectedListener {
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button shiftopen;
    Firebase ref;
    String emailAddress;
    protected String aps;
    String postid,emailAddress_operator;
    private String typeofuser;
    SharedPreferences.Editor editor;
    String localTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_open);
        ref= new Firebase(Constants.FIREBASE_URL);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        emailAddress_operator = preferences.getString("email", null);
        typeofuser=preferences.getString("typeofuser",null);
        emailEditText = (EditText) findViewById(R.id.emailFieldso);
        passwordEditText = (EditText) findViewById(R.id.passwordFieldso);
        shiftopen = (Button) findViewById(R.id.shiftopenbutton);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        List<Integer> slotnum = new ArrayList<Integer>();
        slotnum.add(1);
        slotnum.add(2);
        slotnum.add(3);
        slotnum.add(4);
        slotnum.add(5);
        slotnum.add(6);
        slotnum.add(7);
        slotnum.add(8);
        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, slotnum);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localTime = sdf.format(calendar.getTime());
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
                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Log.d("firebase",ref.toString());
                            // Authenticated successfully with payload authData
                            Query queryRef = ref.child("users").orderByChild("email-address");
                            queryRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                    Log.d("key123",snapshot.getKey());
                                    if(snapshot.getKey().contentEquals("Manager") ){
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            DetailofUser post = snapshot1.getValue(DetailofUser.class);
                                            String emailvalue = post.getEmail().trim();
                                            if(emailvalue.contentEquals(emailAddress)) {
                                                callme();
                                                Intent intent = new Intent(ShiftOpen.this, TypeofOperator.class);
                                                intent.putExtra("UniqueID", postid);
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
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                                // ....
                            });


                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            // Authenticated failed with error firebaseError
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShiftOpen.this);
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

    private void callme() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", emailAddress_operator);
        map.put(typeofuser, "true");
        map.put("Shift Open Time", localTime);
        map.put("aps", aps);
        Log.d("firebase",ref.toString());
        Firebase newpostref=ref.child("users").child("timing").push();
        Log.d("firebase",newpostref.toString());
        Log.d("value of map",map.toString());
        newpostref.setValue(map);
        postid=newpostref.getKey();
        editor.putString("PostID for timing", postid);
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
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(ShiftOpen.this, LoginActivity.class);
        startActivity(i);
    }
}
