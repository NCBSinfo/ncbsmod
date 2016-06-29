package com.secretbiology.ncbsmod.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {

    private List<DashboardModel> entryList;
    View currentview;
    private ClickListener myClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, message;
        LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            currentview = view;
            title = (TextView) view.findViewById(R.id.dashboard_item_title);
            message = (TextView) view.findViewById(R.id.dashboard_item_details);
            layout = (LinearLayout)view.findViewById(R.id.dashboard_item_layout);
            layout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public DashboardAdapter(List<DashboardModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DashboardModel entry = entryList.get(position);

        holder.title.setText(entry.getTitle());
        holder.message.setText(entry.getMessage());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }


}


