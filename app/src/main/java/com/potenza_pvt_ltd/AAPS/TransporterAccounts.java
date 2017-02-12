package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TransporterAccounts extends Activity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mAdapter;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    final ArrayList list = new ArrayList<TruckDetailsActivity>();
    int count = 0;
    int index=0;
    String amount,key;
    ProgressBar pb;
    private LinearLayout linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_accounts);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        Query queryRef = reference.child("users").child("Transporter_Details").orderByChild("Name");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                index = 0;
                count = 0;
                TransporterDetails post = snapshot.getValue(TransporterDetails.class);
                post.setKey(snapshot.getKey());
                Log.d("post", post.getKey());
                TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(), post.getVehicle_no(), post.getAmt());
                list.add(index, obj);
                Log.d("list", String.valueOf(list.get(index)));
                index++;
                Collections.sort(list, new Comparator<TransporterDetails>() {
                    public int compare(TransporterDetails v1, TransporterDetails v2) {
                        return v1.getName().compareTo(v2.getName());
                    }
                });
                mAdapter = new MyRecyclerViewAdapter(list, 2);
                Log.d("count of list", String.valueOf(mAdapter.getItemCount()));
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
                pb.setVisibility(View.GONE);
                linear_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                index = 0;
                count = 0;
                int pos = mAdapter.getPos();
                list.remove(pos);
                TransporterDetails post = dataSnapshot.getValue(TransporterDetails.class);
                post.setKey(dataSnapshot.getKey());
                Log.d("post", post.getKey());
                TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(), post.getVehicle_no(), post.getAmt());
                list.add(pos, obj);
                Log.d("list", String.valueOf(list.get(pos)));
                index++;
                Collections.sort(list, new Comparator<TransporterDetails>() {
                    public int compare(TransporterDetails v1, TransporterDetails v2) {
                        return v1.getName().compareTo(v2.getName());
                    }
                });
                mAdapter = new MyRecyclerViewAdapter(list, 2);
                Log.d("count of list", String.valueOf(mAdapter.getItemCount()));
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(TransporterAccounts.this, Admin.class);
        startActivity(i);    }
}


