package com.secretbiology.ncbsmod.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;

import java.util.List;

public class ModeratorListAdapter extends RecyclerView.Adapter<ModeratorListAdapter.MyViewHolder> {

    private List<String[]> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView topicname, details;
        public LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            topicname = (TextView) view.findViewById(R.id.mod_topic_name);
            details = (TextView) view.findViewById(R.id.mod_topic_details);
            layout = (LinearLayout)view.findViewById(R.id.mod_list_layout);
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



    public ModeratorListAdapter(List<String[]> entryList) {
        this.entryList = entryList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moderator_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String[] entry = entryList.get(position);
        holder.topicname.setText(entry[0]);
        holder.details.setText(entry[1]);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}
