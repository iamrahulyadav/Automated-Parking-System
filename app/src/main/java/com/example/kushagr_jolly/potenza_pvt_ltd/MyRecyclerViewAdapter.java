package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Kushagr_Jolly on 6/9/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TruckDetailsActivity> mDataset;
    private ArrayList<TransporterDetails> mDataset1;
    int value;

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
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(value==1){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_transporter_view, viewGroup, false);
        }
        else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_row_records, viewGroup, false);
        }
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(value==1){
            holder.tv1.setText(mDataset1.get(position).getName());
            holder.tv2.setText(mDataset1.get(position).getAddress());
            holder.tv3.setText(mDataset1.get(position).getSms_no());
            holder.tv4.setText(mDataset1.get(position).getContact_person());
            holder.tv5.setText(mDataset1.get(position).getMobile_no());
            holder.tv6.setText(mDataset1.get(position).getNo_of_vhcl());
            holder.tv7.setText(mDataset1.get(position).getVehicle_no());
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
        if(value==1){
            return mDataset1.size();
        }
        else{
            return mDataset.size();
        }

    }
    public String deleteItem(int index) {
        String key=mDataset.get(index).getKey();
        mDataset.remove(index);
        notifyItemRemoved(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, mDataset.size());
        return key;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView email;
        TextView contractorname;
        TextView drivername;
        TextView drivernumber;
        TextView date;
        TextView aps;
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
        public ViewHolder(View view) {
            super(view);
            if(value==1){
                tv1= (TextView) itemView.findViewById(R.id.textView);
                tv2= (TextView) itemView.findViewById(R.id.textView2);
                tv3= (TextView) itemView.findViewById(R.id.textView3);
                tv4= (TextView) itemView.findViewById(R.id.textView4);
                tv5= (TextView) itemView.findViewById(R.id.textView5);
                tv6= (TextView) itemView.findViewById(R.id.textView6);
                tv7= (TextView) itemView.findViewById(R.id.textView7);

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


}
