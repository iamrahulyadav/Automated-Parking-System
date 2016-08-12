package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateManager extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList values=new ArrayList();
    private ArrayList pwd= new ArrayList();
    private ArrayList uid= new ArrayList();

    Firebase ref;
    ListView listView;
    CustomAdapter customAdapter;
    EditText et2,et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_manager);
        ref=new Firebase(Constants.FIREBASE_URL);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText5);
        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        fetchdata();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

    }

    public void clear(View v){
        et2.setText("");
        et3.setText("");
        fetchdata();
    }

    private void fetchdata() {
        uid.clear();
        values.clear();
        pwd.clear();
        Query queryRef = ref.child("users").child("Manager");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                Log.d("email", post.getEmail());
                Log.d("pass", post.getPwd());
                post.setKey(dataSnapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmail());
                pwd.add(post.getPwd());
                customAdapter = new CustomAdapter(getApplication(), values, pwd,uid,0);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchdata();
                Log.d("change ","is necessary");
                /*
                int pos = customAdapter.getPos();
                Log.d("position to del", String.valueOf(pos));
                values.remove(pos);
                pwd.remove(pos);
                uid.remove(pos);
                DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                post.setKey(dataSnapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmail());
                pwd.add(post.getPwd());
                customAdapter = new CustomAdapter(getApplication(), values, pwd,uid);
                customAdapter.notifyDataSetChanged();
                */
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int pos = customAdapter.getPos();
                values.remove(pos);
                pwd.remove(pos);
                uid.remove(pos);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void save(View v){
        final String email = et2.getText().toString();
        final String pass = et3.getText().toString();
        ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Map<String, Object> value = new HashMap<String, Object>();
                value.put("email-address", email);
                value.put("pass", pass);
                ref.child("users").child("Manager").push().setValue(value);
                Log.d("Successfully", String.valueOf(result.get("uid")));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.title_msg_dailog_box)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        et2.setText("");
        et3.setText("");
    }
    public void search(View v){
        values.clear();
        pwd.clear();
        final String email = et2.getText().toString();
        Query queryRef = ref.child("users").child("Manager").orderByChild("email-address").equalTo(email);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                DetailofUser post = snapshot.getValue(DetailofUser.class);
                post.setKey(snapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmail());
                pwd.add(post.getPwd());
                customAdapter = new CustomAdapter(getApplication(), values, pwd,uid,0);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        if(values.isEmpty()==true &&pwd.isEmpty()==true){
            fetchdata();
            Toast.makeText(getBaseContext(),"No Data Found for this Value",Toast.LENGTH_LONG);
        }

    }
    public void delete(View v){
        final String []item=customAdapter.getValue();
        Query queryRef = ref.child("users").child("Manager").orderByChild("email-address").equalTo(item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                ref.child("users").child("Manager").child(snapshot.getKey()).removeValue();
                customAdapter.notifyDataSetChanged();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ref.removeUser(item[0], item[1], new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                builder.setMessage("User Removed From Database")
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(CreateManager.this, Masters.class);
        startActivity(i);
    }
}
