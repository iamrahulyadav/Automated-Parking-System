package com.potenza_pvt_ltd.AAPS;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public CustomAdapter(Application c, ArrayList strings, ArrayList values) {
        super(c, R.layout.activity_custom_adapter,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
    }
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,ArrayList uid,int num) {
        super(c, R.layout.activity_custom_adapter,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.uid=uid;
        this.flag=num;
    }
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,int num) {
        super(c, R.layout.activity_custom_adapter,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.flag=num;
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
                        Log.d("flag", value[0]);
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
                                    FirebaseAuth mAuth;
                                    final DatabaseReference reference;
                                    mAuth = FirebaseAuth.getInstance();
                                    reference = FirebaseDatabase.getInstance().getReference();
                                    final FirebaseUser user;
                                    user = FirebaseAuth.getInstance().getCurrentUser();

                                    AuthCredential credential = EmailAuthProvider.getCredential(mail, "hello");

                                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.updatePassword(pass[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {

                                                        } else {
                                                            DatabaseReference alanRef = null;
                                                            if (flag == 0) {
                                                                alanRef = reference.child("users").child("Manager").child(key[0]);
                                                            } else if (flag == 1) {
                                                                alanRef = reference.child("users").child("Operator").child(key[0]);
                                                            }
                                                            Map<String, Object> nickname = new HashMap<String, Object>();
                                                            nickname.put("pass", pass[0]);
                                                            alanRef.updateChildren(nickname);
                                                            Toast.makeText(v.getContext(), input.getText(), Toast.LENGTH_SHORT).show();
                                                            Log.d("flag", String.valueOf(flag));
                                                        }
                                                    }
                                                });
                                            } else {

                                            }
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
        Log.d("fdgdf", value[0]+" "+value[1]);
        return value;
    }

    public int getPos(){
        return pos;
    }
}
