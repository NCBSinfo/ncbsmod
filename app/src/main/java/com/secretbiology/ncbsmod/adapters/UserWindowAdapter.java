package com.secretbiology.ncbsmod.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.models.UserDataModel;

import java.util.List;

public class UserWindowAdapter extends RecyclerView.Adapter<UserWindowAdapter.MyViewHolder> {

    private List<UserDataModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView message, timetsmap;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            message = (TextView) view.findViewById(R.id.userwindow_message);
            timetsmap = (TextView) view.findViewById(R.id.userwindow_timestamp);

        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public UserWindowAdapter(List<UserDataModel> entryList) {
        this.entryList = entryList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_window_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserDataModel entry = entryList.get(position);
        holder.message.setText(entry.getMessage());
        holder.timetsmap.setText(entry.getTimestamp() );
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}
