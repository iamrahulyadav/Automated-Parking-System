package com.potenza_pvt_ltd.AAPS;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    final String[] value= new String[2];
    /** Global declaration of variables. As there scope lies in whole class. */
    private Context context;
    private ArrayList<String> email=new ArrayList<String>();
    private ArrayList<String> pwd= new ArrayList<String>();
    private ArrayList<String> code= new ArrayList<String>();
    private int flag;
    boolean b;
    private int pos;

    /** Constructor Class */
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,ArrayList code) {
        super(c, R.layout.activity_custom_adapter ,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.code=code;
    }
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,ArrayList code,int num) {
        super(c,R.layout.activity_custom_adapter ,values);
        Log.d("constructor","constructor");
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.code=code;
        this.flag=num;
        //for(int i=0;i<strings.size();i++){
        //    Log.d("list",email.get(i).toString()+" "+pwd.get(i).toString()+" "+code.get(i).toString());
        //}
    }

    public CustomAdapter(Application application, ArrayList vehicle_type, ArrayList code_value, int i) {
        super(application, R.layout.activity_custom_adapter, vehicle_type);
        this.context=application.getApplicationContext();
        this.email=vehicle_type;
        this.pwd=code_value;
        this.flag=i;
    }

    /** Implement getView method for customizing row of list view. */
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_adapter, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.textview1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.textview2);
        TextView textView3 = (TextView) rowView.findViewById(R.id.textview3);
        if (flag==1) {
            final CheckBox checkBox= (CheckBox) rowView.findViewById(R.id.checkbox);
            if (email.isEmpty() == false && pwd.isEmpty() == false && code.isEmpty() == false) {
                textView1.setText(email.get(position).toString());
                textView2.setText(pwd.get(position).toString());
                textView3.setText(code.get(position).toString());
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        value[0] = email.get(position).toString();
                        value[1] = pwd.get(position).toString();
                        pos=position;
                    }
                }
            });

        }
        if(flag==0){
            final CheckBox checkBox= (CheckBox) rowView.findViewById(R.id.checkbox);
            if (email.isEmpty() == false && pwd.isEmpty() == false && code.isEmpty()==false) {
                textView1.setText(email.get(position).toString());
                textView2.setText(pwd.get(position).toString());
                textView3.setText(code.get(position).toString());
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        value[0] = email.get(position).toString();
                        value[1] = pwd.get(position).toString();
                        pos=position;
                    }
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
