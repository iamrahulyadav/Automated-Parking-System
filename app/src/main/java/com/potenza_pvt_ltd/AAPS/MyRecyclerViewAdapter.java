package com.potenza_pvt_ltd.AAPS;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kushagr_Jolly on 6/9/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<TruckDetailsActivity> mDataset;
    private ArrayList<TransporterDetails> mDataset1;
    int value;
    private String amount;
    private String key;
    private String uniquekey;
    int[][] tar_arr;
    int pos;
    private Firebase ref=new Firebase(Constants.FIREBASE_URL);

    public MyRecyclerViewAdapter(ArrayList<TruckDetailsActivity> myDataset) {
        mDataset = myDataset;
        for (int i=0;i<mDataset.size();i++) {
            Log.d("dataset", mDataset.get(i).getContractorname());
        }
    }
    public MyRecyclerViewAdapter(ArrayList<TransporterDetails> myDataset,int index) {
        mDataset1 = myDataset;
        value=index;
        for (int i=0;i<mDataset1.size();i++) {
            Log.d("dataset", mDataset1.get(i).getName());
        }
    }

    public MyRecyclerViewAdapter(int[][] arr,int i) {
        tar_arr=arr;
        value=i;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(value==1){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_transporter_view, viewGroup, false);
        }
        else if(value==2){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_transporter_accounts_view, viewGroup, false);
        }
        else if(value==3){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_custom_table_tariff, viewGroup, false);
        }
        else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_row_records, viewGroup, false);
        }
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(value==1){
            holder.tv1.setText(mDataset1.get(position).getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos=position;
                }
            });
        }
        else if(value==2) {
            holder.tv1.setText(mDataset1.get(position).getName());
            holder.tv2.setText(mDataset1.get(position).getAddress());
            holder.tv3.setText(mDataset1.get(position).getSms_no());
            holder.tv4.setText(mDataset1.get(position).getContact_person());
            holder.tv5.setText(mDataset1.get(position).getMobile_no());
            holder.tv6.setText(mDataset1.get(position).getNo_of_vhcl());
            holder.tv7.setText(mDataset1.get(position).getVehicle_no());
            holder.tv8.setText(mDataset1.get(position).getAmt());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());

                    alertDialog.setMessage("Enter Amount");
                    final EditText input = new EditText(view.getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Submit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    amount = input.getText().toString();
                                    key = mDataset1.get(position).getKey();
                                    Log.d("pos", String.valueOf(key));
                                    pos=position;
                                    Firebase alanRef = ref.child("users").child("Transporter_Details").child(key);
                                    Map<String, Object> nickname = new HashMap<String, Object>();
                                    nickname.put("Amt", amount);
                                    alanRef.updateChildren(nickname);
                                    Toast.makeText(view.getContext(), input.getText(), Toast.LENGTH_SHORT).show();

                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();
                                }
                            });

                    // closed

                    // Showing Alert Message
                    alertDialog.show();
                }
            });

        }
        else{
            holder.email.setText(mDataset.get(position).getEmail());
            holder.contractorname.setText(mDataset.get(position).getContractorname());
            holder.drivername.setText(mDataset.get(position).getDrivername());
            holder.drivernumber.setText(mDataset.get(position).getDriverno());
            holder.date.setText(mDataset.get(position).getDate());
            holder.aps.setText(mDataset.get(position).getAPS());
        }
    }


    @Override
    public int getItemCount() {
        if(value==1||value==2){
            return mDataset1.size();
        }
        else if(value==3){
            return 10;
        }
        else{
            return mDataset.size();
        }

    }

    public String deleteItem(int index) {
        String key1=mDataset.get(index).getKey();
        mDataset.remove(index);
        notifyItemRemoved(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, mDataset.size());
        notifyDataSetChanged();
        return key1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView email;
        TextView contractorname;
        TextView drivername;
        TextView drivernumber;
        TextView date;
        TextView aps;
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
        GridView gridView;
        CheckBox checkBox;
        public ViewHolder(View view) {
            super(view);
            if(value==1){
                tv1= (TextView) itemView.findViewById(R.id.textView);
            }
            else if(value==2){
                tv1= (TextView) itemView.findViewById(R.id.textView);
                tv2= (TextView) itemView.findViewById(R.id.textView2);
                tv3= (TextView) itemView.findViewById(R.id.textView3);
                tv4= (TextView) itemView.findViewById(R.id.textView4);
                tv5= (TextView) itemView.findViewById(R.id.textView5);
                tv6= (TextView) itemView.findViewById(R.id.textView6);
                tv7= (TextView) itemView.findViewById(R.id.textView7);
                tv8= (TextView) itemView.findViewById(R.id.textView8);
            }
            else if(value==3){
                checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
                gridView=(GridView)itemView.findViewById(R.id.grid);
            }
            else{
                email = (TextView) itemView.findViewById(R.id.textView);
                contractorname = (TextView) itemView.findViewById(R.id.textView2);
                drivername = (TextView) itemView.findViewById(R.id.textView3);
                drivernumber = (TextView) itemView.findViewById(R.id.textView4);
                date = (TextView) itemView.findViewById(R.id.textView5);
                aps = (TextView) itemView.findViewById(R.id.textView6);
            }
        }
    }

    public String getAmt(){
        return amount;
    }

    public String getKey(){
        return uniquekey;
    }
    public int getPos(){
        return pos;
    }

}
