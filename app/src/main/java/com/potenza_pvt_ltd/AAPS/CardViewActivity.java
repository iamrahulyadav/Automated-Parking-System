package com.potenza_pvt_ltd.AAPS;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CardViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private MyRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    int count = 0;
    int index=0;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final ArrayList list = new ArrayList<TruckDetailsActivity>();
    String typeofuser;
    Spinner sp1;
    private String sort;
    private EditText fromDateEtxt;
    private Button d;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private String datefrom,dateto;
    private SimpleDateFormat dateFormatter;
    private String globatime;
    private long globalmillis;
    private long timefrom,timeto;
    ProgressBar pb;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Button logout=(Button)findViewById(R.id.button_logout);
        sp1=(Spinner)findViewById(R.id.spinner1);
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        d=(Button)findViewById(R.id.button5);
        toDateEtxt = (EditText) findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        setDateTimeField();
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
        sp1.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Sort by Date");
        categories.add("Sort by Vehicle Number");
        categories.add("Sort by Driver Name");
        categories.add("Sort by Driver Number");
        categories.add("Sort by Contractor Name");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sp1.setAdapter(dataAdapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        typeofuser=preferences.getString("typeofuser", null);
        /*ref.child("users").child("data").child(mUserId).runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null); // This removes the node.
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError error, boolean b, DataSnapshot data) {
                // Handle completion
            }
        });*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        Log.d("Before", "FetchData");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                datefrom=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                dateto=dateFormatter.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void deletedata(String key, int position) {
        reference.child("users").child("data").child(key).setValue(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sort=parent.getSelectedItem().toString();
        Log.d("abc",sort);
        sort = sort.substring(7, sort.length());
        pb.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        Log.d("abc", sort);
        list.clear();
        index = 0;
        count = 0;
        Query queryRef = reference.child("users").child("data").orderByChild(sort);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("problem", String.valueOf(ds.getValue()));
                    Log.d("post", ds.getKey());
                    TruckDetailsActivity post = ds.getValue(TruckDetailsActivity.class);
                    post.setKey(ds.getKey());
                    TruckDetailsActivity obj = new TruckDetailsActivity(post.getKey(), post.getEmail(), post.getTransporter(), post.getDno(), post.getDate(), post.getAps(),post.getCost(),post.getVtype(),post.getVno(),post.getToa(),post.getTod());
                    list.add(index, obj);
                    Log.d("list", String.valueOf(list.get(index)));
                    index++;
                }
                mAdapter = new MyRecyclerViewAdapter(list);
                mAdapter.notifyDataSetChanged();
                Log.d("count of list", String.valueOf(mAdapter.getItemCount()));
                mRecyclerView.setAdapter(mAdapter);
                pb.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        Log.d("After", "FetchData");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(v == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }

    public void delete(View v){
        Query queryRef = reference.child("users").child("data");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("asda", String.valueOf(dataSnapshot.getChildrenCount()));
                TruckDetailsActivity post = dataSnapshot.getValue(TruckDetailsActivity.class);
                //post.setKey(dataSnapshot.getKey());
                globatime=post.getToa();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a"); // I assume d-M, you may refer to M-d for month-day instead.
                Date d = null; // You will need try/catch around this
                try {
                    d = formatter.parse(globatime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                globalmillis= d.getTime();
                Log.d("globaltime", String.valueOf(globalmillis));
                if(globalmillis>timefrom && globalmillis<timeto){
                    reference.child("users").child("data").child(dataSnapshot.getKey()).setValue(null);
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
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(CardViewActivity.this, Admin.class);
        startActivity(i);
    }

}
