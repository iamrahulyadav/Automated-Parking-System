package com.potenza_pvt_ltd.AAPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class Transporter extends ListActivity {

    protected EditText et1,et2,et3,et4,et5,et6,et7;
    Firebase ref;
    final ArrayList<TransporterDetails> list = new ArrayList<TransporterDetails>();
    int count = 0;
    int index=0;
    private MyRecyclerViewAdapter mAdapter;
    ArrayAdapter <String>adapter;
    ListView l;
    ArrayList<String> name=new ArrayList<String>();
    ArrayList key=new ArrayList<>();
    ArrayList<TransporterDetails> dataset;
    String uid=null;
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
        l=getListView();
         new FetchData(Transporter.this).execute();

    }
    public void save(View v){
        if(uid==null) {
            final String name = et1.getText().toString();
            final String address = et2.getText().toString();
            final String sms = et3.getText().toString();
            final String contact = et4.getText().toString();
            final String mobile = et5.getText().toString();
            final String no_of_vhcl = et6.getText().toString();
            final String vehicle_no = et7.getText().toString();
            if(vehicle_no.contains(",")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
                builder.setMessage("Please Enter Vehicle Number without special characters")
                        .setTitle(R.string.title_msg_dailog_box)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Map<String, Object> value = new HashMap<String, Object>();
                value.put("Name", name);
                value.put("Address", address);
                value.put("sms_no", sms);
                value.put("contact_person", contact);
                value.put("mobile_no", mobile);
                value.put("no_of_vhcl", no_of_vhcl);
                value.put("vehicle_no", vehicle_no);
                ref.child("users").child("Transporter_Details").push().setValue(value);
            }
        }
        else{
            final String name = et1.getText().toString();
            final String address = et2.getText().toString();
            final String sms = et3.getText().toString();
            final String contact = et4.getText().toString();
            final String mobile = et5.getText().toString();
            final String no_of_vhcl = et6.getText().toString();
            Map<String, Object> value = new HashMap<String, Object>();
            value.put("Name", name);
            value.put("Address", address);
            value.put("sms_no", sms);
            value.put("contact_person", contact);
            value.put("mobile_no", mobile);
            value.put("no_of_vhcl", no_of_vhcl);
            ref.child("users").child("Transporter_Details").child(uid).updateChildren(value);

        }
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        et7.setText("");
    }

    public void add(View v){
        String sms=et3.getText().toString();
        final String vehicle_no=et7.getText().toString();
        if(sms==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
            builder.setMessage("Please Enter SMS Number")
                    .setTitle(R.string.title_msg_dailog_box)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(vehicle_no.contains(",")){
            if(vehicle_no.contains(",")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
                builder.setMessage("Please Enter Vehicle Number without special characters")
                        .setTitle(R.string.title_msg_dailog_box)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else {
            Query queryRef = ref.child("users").child("Transporter_Details").orderByChild("sms_no").equalTo(sms);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    Log.d("child", snapshot.getKey());
                    TransporterDetails post = snapshot.getValue(TransporterDetails.class);

                    String vhl_no;
                    if(post.getVehicle_no()==""){
                        vhl_no=vehicle_no;
                    }
                    else {
                        vhl_no = post.getVehicle_no() + "," + vehicle_no;
                    }
                    Map<String, Object> value = new HashMap<String, Object>();
                    value.put("vehicle_no", vhl_no);
                    ref.child("users").child("Transporter_Details").child(snapshot.getKey()).updateChildren(value);

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
    et7.setText("");
    }


    class FetchData extends AsyncTask<Context,String,String> implements AdapterView.OnItemClickListener {
        Activity mActivity;
        public FetchData (Activity activity)
        {
            super();
            mActivity = activity;
        }
        @Override
        protected String doInBackground(Context... params) {
            index = 0;
            count = 0;
            Query queryRef = ref.child("users").child("Transporter_Details").orderByChild("sms_no");
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    Log.d("child", snapshot.getKey());
                    TransporterDetails post = snapshot.getValue(TransporterDetails.class);
                    post.setKey(snapshot.getKey());
                    Log.d("post", post.getKey());
                    TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(), post.getVehicle_no());
                    list.add(index, obj);
                    name.add(index, post.getName());
                    key.add(index, post.getKey());
                    Log.d("list", String.valueOf(list.get(index)));
                    index++;
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    list.clear();
                    name.clear();
                    key.clear();
                    fetchdata();
                    adapter.notifyDataSetChanged();

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
            adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.listview,name);
            l.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            l.setMultiChoiceModeListener(new ModeCallback());
            l.setOnItemClickListener(this);
            l.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dataset=list;
            et1.setText(dataset.get(position).getName());
            et2.setText(dataset.get(position).getAddress());
            et3.setText(dataset.get(position).getSms_no());
            et4.setText(dataset.get(position).getContact_person());
            et5.setText(dataset.get(position).getMobile_no());
            et6.setText(dataset.get(position).getNo_of_vhcl());
            uid=dataset.get(position).getKey();
        }
    }

    private void fetchdata() {
        index = 0;
        count = 0;
        Query queryRef = ref.child("users").child("Transporter_Details").orderByChild("sms_no");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("child", snapshot.getKey());
                TransporterDetails post = snapshot.getValue(TransporterDetails.class);
                post.setKey(snapshot.getKey());
                Log.d("post", post.getKey());
                TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(), post.getVehicle_no());
                list.add(index, obj);
                name.add(index, post.getName());
                key.add(index, post.getKey());
                Log.d("list", String.valueOf(list.get(index)));
                index++;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                list.clear();
                name.clear();
                key.clear();
                fetchdata();
                adapter.notifyDataSetChanged();
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
    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.del:
                    final SparseBooleanArray checked=l.getCheckedItemPositions();
                    for (int i = 0; i < l.getAdapter().getCount(); i++) {
                        if (checked.get(i)) {
                            // Do something
                            Log.d("key", String.valueOf(key.get(i)));
                            ref.child("users").child("Transporter_Details").child(String.valueOf(key.get(i))).removeValue();
                        }
                    }
                    Toast.makeText(Transporter.this, "Deleted" + getListView().getCheckedItemCount() +
                            " items", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    break;
                default:
                    Toast.makeText(Transporter.this, "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }

    }
}
