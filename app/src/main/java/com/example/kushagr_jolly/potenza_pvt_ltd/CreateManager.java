package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateManager extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList values=new ArrayList();
    private ArrayList pwd= new ArrayList();
    private ArrayList code= new ArrayList();
    Firebase ref;
    ListView listView;
    CustomAdapter customAdapter;
    EditText et1,et2,et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_manager);
        ref=new Firebase(Constants.FIREBASE_URL);
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText5);
        listView = (ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        Query queryRef = ref.child("users").child("Manager");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                    DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                    Log.d("email",post.getEmail());
                    Log.d("pass",post.getPwd());
                    values.add(post.getEmail());
                    pwd.add(post.getPwd());
                    code.add(post.getCode());
                    customAdapter = new CustomAdapter(getApplication(), values, pwd,code);
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

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void clear(View v){
        et1.setText("");
        et2.setText("");
        et3.setText("");
    }
    public void save(View v){
        final String code = et1.getText().toString();
        final String email = et2.getText().toString();
        final String pass = et3.getText().toString();
        ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Map<String, Object> value = new HashMap<String, Object>();
                value.put("email-address", email);
                value.put("code",code);
                value.put("pass",pass);
                ref.child("users").child("Manager").push().setValue(value);
                Log.d("Successfully", String.valueOf(result.get("uid")));
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
    public void search(View v){
        values.clear();
        pwd.clear();
        final String email = et2.getText().toString();
        Query queryRef = ref.child("users").child("Manager").orderByChild("emailID").equalTo(email);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                DetailofUser post = snapshot.getValue(DetailofUser.class);
                values.add(post.getEmail());
                pwd.add(post.getPwd());
                code.add(post.getCode());
                customAdapter = new CustomAdapter(getApplication(), values, pwd,code);
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
}
