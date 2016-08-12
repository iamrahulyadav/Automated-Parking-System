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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateOperator extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList values=new ArrayList();
    private ArrayList pwd= new ArrayList();
    Firebase ref;
    ListView listView;
    CustomAdapter customAdapter;
    EditText et2,et3;
    private ArrayList uid= new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_operator);
        ref=new Firebase(Constants.FIREBASE_URL);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText5);
        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        fetchdata();

    }

    private void fetchdata() {
        Query queryRef = ref.child("users").child("Operator");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                post.setKey(dataSnapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmail());
                pwd.add(post.getPwd());
                customAdapter = new CustomAdapter(getApplication(), values, pwd,uid,1);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                values.clear();
                pwd.clear();
                uid.clear();
                fetchdata();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                values.clear();
                pwd.clear();
                uid.clear();
                fetchdata();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void clear(View v){
        et2.setText("");
        et3.setText("");
        fetchdata();
    }
    public void save(View v){
        String email = et2.getText().toString();
        final String pass = et3.getText().toString();
        email=email.toLowerCase();
        final String finalEmail = email;
        ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Map<String, Object> value = new HashMap<String, Object>();
                value.put("email-address", finalEmail);
                value.put("pass", pass);
                ref.child("users").child("Operator").push().setValue(value);
                Log.d("Successfully", String.valueOf(result.get("uid")));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOperator.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
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
        uid.clear();
        final String email = et2.getText().toString();
        Query queryRef = ref.child("users").child("Operator").orderByChild("email-address").equalTo(email);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                DetailofUser post = snapshot.getValue(DetailofUser.class);
                post.setKey(snapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmail());
                pwd.add(post.getPwd());
                customAdapter = new CustomAdapter(getApplication(), values, pwd, uid,1);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOperator.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        if(values.isEmpty()==true && values.isEmpty()==false)
            fetchdata();
    }
    public void delete(View v){
        final String []item=customAdapter.getValue();
        Query queryRef = ref.child("users").child("Operator").orderByChild("email-address").equalTo(item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                ref.child("users").child("Operator").child(snapshot.getKey()).removeValue();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOperator.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOperator.this);
                builder.setMessage("User Removed From Database")
                        .setTitle(R.string.title_msg_dailog_box)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateOperator.this);
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
        Intent i = new Intent(CreateOperator.this, Masters.class);
        startActivity(i);
    }
}
