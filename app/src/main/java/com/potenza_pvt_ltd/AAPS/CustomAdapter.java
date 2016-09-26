package com.potenza_pvt_ltd.AAPS;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter extends ArrayAdapter<String> {
    final String[] value= new String[2];

    /** Global declaration of variables. As there scope lies in whole class. */
    private Context context;
    int [][] tar_arr;
    private ArrayList<String> email=new ArrayList<String>();
    private ArrayList<String> pwd= new ArrayList<String>();
    private ArrayList<String> uid= new ArrayList<String>();
    private int flag;
    int pos;

    /** Constructor Class */
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,ArrayList uid) {
        super(c, R.layout.activity_custom_adapter,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.uid=uid;
    }
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,ArrayList uid,int num) {
        super(c, R.layout.activity_custom_adapter,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.uid=uid;
        this.flag=num;
    }

    public CustomAdapter(Application application, ArrayList<String> psname, ArrayList<String> num,int f) {
        super(application, R.layout.activity_custom_adapter, psname);
        this.email=psname;
        this.pwd=num;
        this.flag=f;
    }

    /** Implement getView method for customizing row of list view. */
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("flag", String.valueOf(flag));
        View rowView;
        if(flag==10){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ps_list, parent, false);
            TextView textView1 = (TextView) rowView.findViewById(R.id.textview1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.textview2);
            if(email.isEmpty()==false && pwd.isEmpty()==false){
                textView1.setText(email.get(position).toString());
                textView2.setText(pwd.get(position).toString());
            }
        }
        else if (flag == 3) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_adapter1, parent, false);
            TextView textView1 = (TextView) rowView.findViewById(R.id.textview1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.textview2);
            final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
            if (email.isEmpty() == false && pwd.isEmpty() == false) {
                textView1.setText(email.get(position).toString());
                Log.d("pos in customadpter", String.valueOf(position));
                textView2.setText(pwd.get(position).toString());
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        value[0] = email.get(position).toString();
                        value[1] = pwd.get(position).toString();
                        pos = position;
                    }
                }
            });
        }
        else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_adapter, parent, false);
            TextView textView1 = (TextView) rowView.findViewById(R.id.textview1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.textview2);
            final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
            if (email.isEmpty() == false && pwd.isEmpty() == false) {
                textView1.setText(email.get(position).toString());
                textView2.setText(pwd.get(position).toString());
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        value[0] = email.get(position).toString();
                        value[1] = pwd.get(position).toString();
                        pos = position;
                    }
                }
            });
        }

        if (flag == 0 || flag == 1) {
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final String[] pass = new String[1];
                    final String[] key = new String[1];
                    android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(v.getContext());

                    alertDialog.setMessage("Please Enter Updated Password");
                    final EditText input = new EditText(v.getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your uid here to execute after dialog
                                    String mail = String.valueOf(email.get(position));
                                    pass[0] = input.getText().toString();
                                    key[0] = String.valueOf(uid.get(position));
                                    pos = position;
                                    Log.d("pos", String.valueOf(key[0]));
                                    final Firebase ref = new Firebase(Constants.FIREBASE_URL);
                                    ref.changePassword(mail, String.valueOf(pwd.get(position)), pass[0], new Firebase.ResultHandler() {
                                        @Override
                                        public void onSuccess() {
                                            // password changed
                                            Firebase alanRef = null;
                                            if (flag == 0) {
                                                alanRef = ref.child("users").child("Manager").child(key[0]);
                                            } else if (flag == 1) {
                                                alanRef = ref.child("users").child("Operator").child(key[0]);
                                            }
                                            Map<String, Object> nickname = new HashMap<String, Object>();
                                            nickname.put("pass", pass[0]);
                                            alanRef.updateChildren(nickname);
                                            Toast.makeText(v.getContext(), input.getText(), Toast.LENGTH_SHORT).show();
                                            Log.d("flag", String.valueOf(flag));
                                        }

                                        @Override
                                        public void onError(FirebaseError firebaseError) {
                                            // error encountered
                                        }
                                    });


                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your uid here to execute after dialog
                                    dialog.cancel();
                                }
                            });

                    // closed

                    // Showing Alert Message
                    alertDialog.show();

                }
            });
        }
        return rowView;
    }

    public String[] getValue(){
        return value;
    }

    public int getPos(){
        return pos;
    }
}
