package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kushagr_Jolly on 6/9/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<TruckDetailsActivity> mDataset;



    public MyRecyclerViewAdapter(ArrayList<TruckDetailsActivity> myDataset) {
        mDataset = myDataset;
    }
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_row, viewGroup, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.email.setText(mDataset.get(position).getEmail());
        holder.contractorname.setText(mDataset.get(position).getContractorname());
        holder.drivername.setText(mDataset.get(position).getDrivername());
        holder.drivernumber.setText(mDataset.get(position).getDriverno());
        holder.date.setText(mDataset.get(position).getDate());
        holder.aps.setText(mDataset.get(position).getAPS());
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
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

        public ViewHolder(View view) {
            super(view);
            email = (TextView) itemView.findViewById(R.id.textView);
            contractorname = (TextView) itemView.findViewById(R.id.textView2);
            drivername = (TextView) itemView.findViewById(R.id.textView3);
            drivernumber = (TextView) itemView.findViewById(R.id.textView4);
            date = (TextView) itemView.findViewById(R.id.textView5);
            aps = (TextView) itemView.findViewById(R.id.textView6);
        }
    }


}
