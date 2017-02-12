package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateManager extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList values=new ArrayList();
    private ArrayList pwd= new ArrayList();
    private ArrayList uid= new ArrayList();

    private FirebaseAuth mAuth;
    DatabaseReference reference;
    ListView listView;
    CustomAdapter customAdapter;
    EditText et2,et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_manager);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();        et2=(EditText)findViewById(R.id.editText2);
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
        Query queryRef = reference.child("users").child("Manager");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                DetailofUser post = dataSnapshot.getValue(DetailofUser.class);
                Log.d("email", post.getEmailaddress());
                Log.d("pass", post.getPass());
                post.setKey(dataSnapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmailaddress());
                pwd.add(post.getPass());
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
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

    }

    public void save(View v){
        final String email = et2.getText().toString();
        final String pass = et3.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(CreateManager.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Map<String, Object> value = new HashMap<String, Object>();
                        value.put("emailaddress", email);
                        value.put("pass", pass);
                        reference.child("users").child("Manager").push().setValue(value);
                        if (!task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                            builder.setMessage((CharSequence) task.getException())
                                    .setTitle(R.string.title_msg_dailog_box)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
        et2.setText("");
        et3.setText("");
    }
    public void search(View v){
        values.clear();
        pwd.clear();
        final String email = et2.getText().toString();
        Query queryRef = reference.child("users").child("Manager").orderByChild("emailaddress").equalTo(email);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                DetailofUser post = snapshot.getValue(DetailofUser.class);
                post.setKey(snapshot.getKey());
                uid.add(post.getKey());
                values.add(post.getEmailaddress());
                pwd.add(post.getPass());
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
            public void onCancelled(DatabaseError firebaseError) {
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
        Query queryRef = reference.child("users").child("Manager").orderByChild("emailaddress").equalTo(item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                reference.child("users").child("Manager").child(snapshot.getKey()).removeValue();
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
            public void onCancelled(DatabaseError firebaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                            builder.setMessage("User Removed From Database")
                                    .setTitle(R.string.title_msg_dailog_box)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateManager.this);
                            builder.setMessage((CharSequence) task.getException())
                                    .setTitle(R.string.login_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
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
