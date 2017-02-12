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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Collections extends Activity {

    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private int cost = 0;
    TextView text;
    EditText emailid, pass;
    private String email;
    private String emailAddress;
    private String formattedDate;
    private Button cash_handover;
    ProgressBar pb;
    private LinearLayout linear_layout;
    private String localTime;
    private String finaltime;
    private SimpleDateFormat sdf1;
    Date date = null;
    Date dat = null;
    private long millis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date=new Date();
        dat=new Date();
        setContentView(R.layout.activity_collections);
        text = (TextView) findViewById(R.id.tot_amt_cal);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        email = prefs.getString("email", "");
        emailid = (EditText) findViewById(R.id.emailField);
        pass = (EditText) findViewById(R.id.passwordField);
        cash_handover=(Button)findViewById(R.id.cashhandover);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
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
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        formattedDate = String.valueOf(new StringBuilder().append(dd).append("/").append(mm + 1).append("/").append(yy));
        sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date1=null;
        try {
            date1 = sdf1.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        localTime = sdf.format(calendar.getTime());
        Query qr=reference.child("users").child("cash-handover").orderByChild("Date");
        final Date finalDate = date1;
        finaltime=formattedDate+" "+localTime;
        final int[] count = {0};
        qr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("count child", String.valueOf(dataSnapshot.getChildrenCount()));
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    count[0]++;
                    if(count[0] ==dataSnapshot.getChildrenCount()) {
                        Log.d("key-collections", ds.getKey());
                        TruckDetailsActivity truckDetailsActivity = ds.getValue(TruckDetailsActivity.class);
                        String dat_time = truckDetailsActivity.getDate() + " " + truckDetailsActivity.getToa();
                        DateTimeZone timeZone = DateTimeZone.forID("Asia/Kolkata"); // Or, DateTimeZone.UTC
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm:ss a");
                        DateTime dateTime = formatter.withZone(timeZone).parseDateTime(dat_time);
                        millis = dateTime.getMillis();
                        Log.d("millis", String.valueOf(millis));
                        pb.setVisibility(View.GONE);
                        linear_layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        qr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


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
        getData();
        cash_handover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Collections.this, TypeofOperator.class);
        startActivity(i);
    }


    public void newActivity() {
        String email = emailid.getText().toString();
        String password = pass.getText().toString();

        email = email.trim();
        password = password.trim();

        if (email.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Collections.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            emailAddress = email;
            Log.d("Email", email);
            Log.d("Password", password);

            //Login with an email/password combination
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Collections.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Authenticated successfully with payload authData
                                    Query queryRef = reference.child("users").orderByChild("email-address");
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
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Collections.this);
                                                        builder.setMessage("The money has been handed over to the manager.")
                                                                .setTitle("Successful")
                                                                .setPositiveButton(android.R.string.ok, null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                        emailid.setText("");
                                                        pass.setText("");
                                                        text.setText(String.valueOf(0));
                                                        //Intent intent = new Intent(Collections.this, TypeofOperator.class);
                                                        //startActivity(intent);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(Collections.this);
                                builder.setMessage((CharSequence) task.getException())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }



                                }
                            });
        }
    }
    private void callme() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email);
        map.put("Amount Collected", cost);
        map.put("Date", formattedDate);
        map.put("Cash Handover", localTime);
        DatabaseReference newpostref=reference.child("users").child("cash-handover").push();
        newpostref.setValue(map);
    }

    public void getData() {
        pb.setVisibility(View.VISIBLE);
        linear_layout.setVisibility(View.GONE);
        Query queryRef = reference.child("users").child("data").orderByChild("email").equalTo(email);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TruckDetailsActivity post = dataSnapshot.getValue(TruckDetailsActivity.class);
                String dat_time1 = post.getDate() + " " + post.getToa();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                Date dat1 = null;
                try {
                    dat1 = sdf.parse(dat_time1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("time of cash handover", String.valueOf(millis));
                Log.d("time of data", String.valueOf(dat1.getTime()));
                if (dat1.getTime()> millis) {
                    cost += Integer.parseInt(post.getCost());
                }
                text.setText(String.valueOf(cost));
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
        text.setText(String.valueOf(cost));

    }
}