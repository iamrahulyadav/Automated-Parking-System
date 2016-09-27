package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

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
    Firebase ref= new Firebase(Constants.FIREBASE_URL);
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
        setDateTimeField();
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
                ref.unauth();
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
        ref.child("users").child("data").child(key).setValue(null);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sort=parent.getSelectedItem().toString();
        Log.d("abc",sort);
        sort = sort.substring(7, sort.length());
        Log.d("abc", sort);
        new FetchData(CardViewActivity.this).execute();
        Log.d("After", "FetchData");
        mAdapter = new MyRecyclerViewAdapter(list);
        /*if(typeofuser.contentEquals("Admin")) {
            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(mRecyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipeLeft(int position) {
                                    return false;
                                }

                                @Override
                                public boolean canSwipeRight(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {

                                        mAdapter.notifyItemRemoved(position);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        Log.d("Hello", "right");
                                        String key =mAdapter.deleteItem(position);
                                        deletedata(key,position);
                                        mAdapter.notifyItemRemoved(position);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            });

            mRecyclerView.addOnItemTouchListener(swipeTouchListener);
        }
        */
        mRecyclerView.setAdapter(mAdapter);

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

    class FetchData extends AsyncTask<Context,String,String>{
        Activity mActivity;
    public FetchData (Activity activity)
    {
        super();
        mActivity = activity;
    }
    @Override
    protected String doInBackground(Context... params) {
        list.clear();
        index = 0;
        count = 0;
       Query queryRef = ref.child("users").child("data").orderByChild(sort);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    count = (int) snapshot.getChildrenCount();
                    if (count == 9) {
                        Log.d("count", String.valueOf(count));
                        Log.d("sas", snapshot.getKey());
                        TruckDetailsActivity post = snapshot.getValue(TruckDetailsActivity.class);
                        post.setKey(snapshot.getKey());
                        Log.d("post", post.getKey());
                        TruckDetailsActivity obj = new TruckDetailsActivity(post.getKey(), post.getEmail(), post.getContractorname(), post.getDrivername(), post.getDriverno(), post.getDate(), post.getAPS());
                        list.add(index, obj);
                        Log.d("list", String.valueOf(list.get(index)));
                        index++;
                    }
                Log.d("count of list", String.valueOf(mAdapter.getItemCount()));
                mAdapter.notifyDataSetChanged();

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
        });
        return null;
    }

    @Override
    protected void onPreExecute(){
        Log.d("Value of sort before:",sort);
    }


    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

    public void delete(View v){
        Query queryRef = ref.child("users").child("data");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("asda", String.valueOf(dataSnapshot.getChildrenCount()));
                TruckDetailsActivity post = dataSnapshot.getValue(TruckDetailsActivity.class);
                //post.setKey(dataSnapshot.getKey());
                globatime=post.gettime();
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
                    ref.child("users").child("data").child(dataSnapshot.getKey()).setValue(null);
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
