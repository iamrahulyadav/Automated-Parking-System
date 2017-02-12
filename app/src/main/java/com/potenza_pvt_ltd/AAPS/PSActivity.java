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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PSActivity extends Activity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    private EditText et1,et3;
    private ListView listView;
    private CustomAdapter customAdapter;
    String code_value_1;
    private ArrayList slot_name,slot_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ps);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();        et1=(EditText)findViewById(R.id.editText6);
        et3=(EditText)findViewById(R.id.editText7);
        slot_name=new ArrayList();
        slot_number= new ArrayList();
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        customAdapter = new CustomAdapter(getApplication(), slot_name,slot_number);
        listView.setAdapter(customAdapter);
        Query queryRef = reference.child("users").child("Parking_Slot_Details");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("value of", String.valueOf(dataSnapshot.getKey()));
                ParkingSlotDetails post = dataSnapshot.getValue(ParkingSlotDetails.class);
                slot_name.add(post.getSlot_name());
                slot_number.add(post.getSlot_number());
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int pos = customAdapter.getPos();
                slot_name.remove(pos);
                slot_number.remove(pos);
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
        final String code = code_value_1;
        final String vehicle = et1.getText().toString();
        final String inslip = et3.getText().toString();
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("slot_name",vehicle);
        value.put("slot_number",inslip);
        reference.child("users").child("Parking_Slot_Details").push().setValue(value);
        customAdapter.notifyDataSetChanged();
    }


    public void delete(View v){
        final String []item=customAdapter.getValue();
        Query queryRef = reference.child("users").child("Parking_Slot_Details").orderByChild("slot_name").equalTo(item[0]);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("delte func", String.valueOf(item));
                reference.child("users").child("Parking_Slot_Details").child(snapshot.getKey()).removeValue();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PSActivity.this);
                builder.setMessage(firebaseError.getMessage())
                        .setTitle(R.string.login_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        code_value_1=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        customAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(PSActivity.this, Masters.class);
        startActivity(i);
    }
}
