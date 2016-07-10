package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    final String[] value= new String[2];
    /** Global declaration of variables. As there scope lies in whole class. */
    private Context context;
    private ArrayList email=new ArrayList();
    private ArrayList pwd= new ArrayList();
    private ArrayList code= new ArrayList();

    /** Constructor Class */
    public CustomAdapter(Application c, ArrayList strings, ArrayList values,ArrayList code) {
        super(c,R.layout.activity_custom_adapter ,values);
        this.context = c.getApplicationContext();
        this.email = strings;
        this.pwd=values;
        this.code=code;
    }


    /** Implement getView method for customizing row of list view. */
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_adapter, parent, false);
        TextView textView1 = (TextView)rowView.findViewById(R.id.textview1);
        TextView textView2 = (TextView)rowView.findViewById(R.id.textview2);
        TextView textView3=(TextView)rowView.findViewById(R.id.textview3);
        RadioButton radioButton = (RadioButton) rowView.findViewById(R.id.radiobutton);
        if(email!=null || pwd!=null){
            textView3.setText(code.get(position).toString());
            textView1.setText(email.get(position).toString());
            textView2.setText(pwd.get(position).toString());
        }
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value[0]=email.get(position).toString();
                value[1]=pwd.get(position).toString();
            }
        });

        return rowView;
    }
    public String[] getValue(){
        return value;
    }
}
