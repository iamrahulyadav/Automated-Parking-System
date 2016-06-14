package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kushagr_Jolly on 6/9/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<TruckDetailsActivity> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView email;
        TextView contractorname;
        TextView drivername;
        TextView drivernumber;
        TextView date;
        TextView aps;


        public DataObjectHolder(View itemView) {
            super(itemView);
            Log.d("entering activity", "entering");
            email = (TextView) itemView.findViewById(R.id.textView);
            contractorname = (TextView) itemView.findViewById(R.id.textView2);
            drivername = (TextView) itemView.findViewById(R.id.textView3);
            drivernumber = (TextView) itemView.findViewById(R.id.textView4);
            date = (TextView) itemView.findViewById(R.id.textView5);
            aps = (TextView) itemView.findViewById(R.id.textView6);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<TruckDetailsActivity> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
      // Log.d("asd",mDataset.get(position).getEmail());
        holder.email.setText(mDataset.get(position).getEmail());
        holder.contractorname.setText(mDataset.get(position).getContractorname());
        holder.drivername.setText(mDataset.get(position).getDrivername());
        holder.drivernumber.setText(mDataset.get(position).getDriverno());
        holder.date.setText(mDataset.get(position).getDate());
        holder.aps.setText(mDataset.get(position).getAPS());
    }

    public void addItem(TruckDetailsActivity dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
