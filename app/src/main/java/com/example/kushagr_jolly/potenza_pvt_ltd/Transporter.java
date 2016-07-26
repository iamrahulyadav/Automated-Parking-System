package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transporter extends Activity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    protected EditText et1,et2,et3,et4,et5,et6,et7;
    Firebase ref;
    final ArrayList list = new ArrayList<TruckDetailsActivity>();
    int count = 0;
    int index=0;
    private MyRecyclerViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter);
        ref= new Firebase(Constants.FIREBASE_URL);
        et1=(EditText)findViewById(R.id.editText8);
        et2=(EditText)findViewById(R.id.editText9);
        et3=(EditText)findViewById(R.id.editText10);
        et4=(EditText)findViewById(R.id.editText11);
        et5=(EditText)findViewById(R.id.editText12);
        et6=(EditText)findViewById(R.id.editText13);
        et7=(EditText)findViewById(R.id.editText14);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
        new FetchData(Transporter.this).execute();

    }
    public void save(View v){
        final String name = et1.getText().toString();
        final String address = et2.getText().toString();
        final String sms = et3.getText().toString();
        final String contact = et4.getText().toString();
        final String mobile = et5.getText().toString();
        final String no_of_vhcl = et6.getText().toString();
        final String vehicle_no = et7.getText().toString();

        Map<String, Object> value = new HashMap<String, Object>();
        value.put("Name",name);
        value.put("Address", address);
        value.put("sms_no", sms);
        value.put("contact_person",contact);
        value.put("mobile_no", mobile);
        value.put("no_of_vhcl", no_of_vhcl);
        value.put("vehicle_no", vehicle_no);

        ref.child("users").child("Transporter_Details").push().setValue(value);
        //customAdapter.notifyDataSetChanged();
    }
    private void deletedata(String key, int position) {
        ref.child("users").child("Transporter_Details").child(key).setValue(null);
    }
    public void delete(View v){
        final String vehicle_no=et7.getText().toString();
        Query queryRef = ref.child("users").child("Transporter_Details").orderByChild("vehicle_no").equalTo(vehicle_no);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("delte func", String.valueOf(vehicle_no));
                ref.child("users").child("Transporter_Details").child(snapshot.getKey()).removeValue();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        mAdapter.notifyDataSetChanged();
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
                    TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(),post.getVehicle_no());
                    list.add(index, obj);
                    Log.d("list", String.valueOf(list.get(index)));
                    index++;
                    mAdapter = new MyRecyclerViewAdapter(list,1);
                    Log.d("count of list", String.valueOf(mAdapter.getItemCount()));
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    int pos=mAdapter.getPos();
                    list.remove(pos);
                    Log.d("child", dataSnapshot.getKey());
                    TransporterDetails post = dataSnapshot.getValue(TransporterDetails.class);
                    post.setKey(dataSnapshot.getKey());
                    Log.d("post", post.getKey());
                    TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(),post.getVehicle_no(),post.getAmt());
                    list.add(pos, obj);
                    Log.d("list", String.valueOf(list.get(pos)));
                    index++;
                    mAdapter = new MyRecyclerViewAdapter(list,1);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(Transporter.this, Masters.class);
        startActivity(i);
    }

}
