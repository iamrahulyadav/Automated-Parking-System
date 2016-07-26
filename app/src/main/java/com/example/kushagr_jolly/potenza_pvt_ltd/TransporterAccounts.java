package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransporterAccounts extends Activity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mAdapter;
    Firebase ref=new Firebase(Constants.FIREBASE_URL);
    final ArrayList list = new ArrayList<TruckDetailsActivity>();
    int count = 0;
    int index=0;
    String amount,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_accounts);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new FetchData(TransporterAccounts.this).execute();
    }
    class FetchData extends AsyncTask<Context,String,String> {
        Activity mActivity;
        public FetchData (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            Query queryRef = ref.child("users").child("Transporter_Details").orderByChild("sms_no");
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    index = 0;
                    count = 0;
                    Log.d("child", snapshot.getKey());
                    TransporterDetails post = snapshot.getValue(TransporterDetails.class);
                    post.setKey(snapshot.getKey());
                    Log.d("post", post.getKey());
                    TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(),post.getVehicle_no(),post.getAmt());
                    list.add(index, obj);
                    Log.d("list", String.valueOf(list.get(index)));
                    index++;
                    mAdapter = new MyRecyclerViewAdapter(list,2);
                    Log.d("count of list", String.valueOf(mAdapter.getItemCount()));
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    index = 0;
                    count = 0;
                    int pos=mAdapter.getPos();
                    list.remove(pos);
                    TransporterDetails post = dataSnapshot.getValue(TransporterDetails.class);
                    post.setKey(dataSnapshot.getKey());
                    Log.d("post", post.getKey());
                    TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(),post.getVehicle_no(),post.getAmt());
                    list.add(pos, obj);
                    Log.d("list", String.valueOf(list.get(pos)));
                    index++;
                    mAdapter = new MyRecyclerViewAdapter(list,2);
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
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute(){
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


