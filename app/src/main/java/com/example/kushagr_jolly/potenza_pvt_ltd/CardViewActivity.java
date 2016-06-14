package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CardViewActivity extends AppCompatActivity {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    int count = 0;
    int index=0;
    Firebase ref= new Firebase(Constants.FIREBASE_URL);
    final ArrayList list = new ArrayList<TruckDetailsActivity>();
    String typeofuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        String mUserId = getIntent().getStringExtra("UniqueID");
        typeofuser=getIntent().getStringExtra("typeofuser");
        ref.child("users").child(mUserId).runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null); // This removes the node.
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError error, boolean b, DataSnapshot data) {
                // Handle completion
            }
        });
        new fetchdata(CardViewActivity.this).execute();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

class fetchdata extends AsyncTask<Context,String,String>{

    Context ApplicationContext;
    Activity mActivity;
    public fetchdata (Activity activity)
    {
        super();
        mActivity = activity;
    }
    @Override
    protected String doInBackground(Context... params) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("key",dataSnapshot.getKey());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    count= (int) postSnapshot.getChildrenCount();
                    Log.d("count", String.valueOf(count));
                    for (DataSnapshot postpostSnapshot : postSnapshot.getChildren()) {
                        Log.d("sas",postpostSnapshot.getKey());
                        TruckDetailsActivity post = postpostSnapshot.getValue(TruckDetailsActivity.class);
                        Log.d("post", String.valueOf(post));
                        TruckDetailsActivity obj = new TruckDetailsActivity(post.getEmail(),post.getContractorname(),post.getDrivername(),post.getDriverno(),post.getDate(),post.getAPS());
                        list.add(index, obj);
                        Log.d("list", String.valueOf(list.get(index)));
                        index++;
                    }
                }
                mAdapter = new MyRecyclerViewAdapter(list);
                mRecyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApplicationContext);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
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
