package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingSlot extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected EditText contractorname;
    protected EditText drivername;
    protected EditText driverno;
    protected EditText vehicleno;
    protected TextView date;
    protected Button submitButton;
    protected EditText search;
    protected ImageButton imageButton;
    private Firebase mRef;
    private String mUserId;
    protected String aps;
    private String useridold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_slot);
        mUserId=getIntent().getStringExtra("UniqueID");
        Log.d("xyz", mUserId);
        // Check Authentication
        mRef = new Firebase(Constants.FIREBASE_URL);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        List<Integer> categories = new ArrayList<Integer>();
        categories.add(1);
        categories.add(2);
        categories.add(3);
        categories.add(4);
        categories.add(5);
        categories.add(6);
        categories.add(7);
        categories.add(8);
        search=(EditText)findViewById(R.id.editText_search);
        imageButton=(ImageButton)findViewById(R.id.imageButton);
        contractorname= (EditText) findViewById(R.id.editText_ps);
        drivername= (EditText) findViewById(R.id.editText2_ps);
        driverno= (EditText) findViewById(R.id.editText3_ps);
        vehicleno = (EditText) findViewById(R.id.editText4_ps);
        date= (TextView) findViewById(R.id.textView_date1_ps);
        submitButton= (Button) findViewById(R.id.button_submit_ps);
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // set current date into textview
        date.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(yy).append("-").append(mm + 1).append("-")
                .append(dd));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String vhlno = search.getText().toString();
                Log.d("vehicle", vhlno);

                // Attach an listener to read the data at our posts reference
                Query queryRef = mRef.child("users").orderByChild("Vehicle Number").equalTo(vhlno);
                queryRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        useridold=snapshot.getKey();
                        TruckDetailsActivity truck = snapshot.getValue(TruckDetailsActivity.class);
                        contractorname.setText(truck.getContractorname());
                        drivername.setText(truck.getDrivername());
                        driverno.setText(truck.getDriverno());
                        vehicleno.setText(truck.getVehicleno());
                        date.setText(truck.getDate());
                        String localtime=truck.getTime();
                        Log.d("askjdh",localtime);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ParkingSlot.this);
                        builder.setMessage(firebaseError.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                });
                Log.d("alert", "not printed");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("aps", aps);
                mRef.child("users").child(useridold).updateChildren(graceNickname);
            }
        });

        mRef.child("users").child(mUserId).runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(null); // This removes the node.
                return Transaction.success(mutableData);
            }

            public void onComplete(FirebaseError error, boolean b, DataSnapshot data) {
                // Handle completion
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parking_slot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        aps= parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
