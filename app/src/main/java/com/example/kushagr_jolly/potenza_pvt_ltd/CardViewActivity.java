package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

public class CardViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        String mUserId = getIntent().getStringExtra("UniqueID");
        Button logout=(Button)findViewById(R.id.button_logout);
        sp1=(Spinner)findViewById(R.id.spinner1);
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
        typeofuser=getIntent().getStringExtra("typeofuser");
        ref.child("users").child("data").child(mUserId).runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null); // This removes the node.
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError error, boolean b, DataSnapshot data) {
                // Handle completion
            }
        });
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

        if(typeofuser.contentEquals("Admin")) {
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

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
       Query queryRef = ref.child("users").orderByChild(sort);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("child", snapshot.getKey());
                list.clear();
                index = 0;
                count = 0;
                for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                    count = (int) postsnapshot.getChildrenCount();
                    if (count == 9) {
                        Log.d("count", String.valueOf(count));
                        Log.d("sas", postsnapshot.getKey());
                        TruckDetailsActivity post = postsnapshot.getValue(TruckDetailsActivity.class);
                        post.setKey(postsnapshot.getKey());
                        Log.d("post", post.getKey());
                        TruckDetailsActivity obj = new TruckDetailsActivity(post.getKey(), post.getEmail(), post.getContractorname(), post.getDrivername(), post.getDriverno(), post.getDate(), post.getAPS());
                        list.add(index, obj);
                        Log.d("list", String.valueOf(list.get(index)));
                        index++;
                    }
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

}
