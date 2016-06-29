package com.secretbiology.ncbsmod.topics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.database.models.NotificationModel;

import java.util.List;

public class TopicWindowAdapter extends RecyclerView.Adapter<TopicWindowAdapter.MyViewHolder> {

    private List<NotificationModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView message, timetsmap, title;
        public LinearLayout layout;

        public MyViewHolder(View view) {
            super(view);
            currentview = view;
            message = (TextView) view.findViewById(R.id.topicwindow_message);
            timetsmap = (TextView) view.findViewById(R.id.topicwindow_timestamp);
            layout = (LinearLayout) view.findViewById(R.id.topic_item_layout);
            title = (TextView)view.findViewById(R.id.topicwindow_item_title);


        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public TopicWindowAdapter(List<NotificationModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_window_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationModel entry = entryList.get(position);
        holder.message.setText(entry.getMessage());
        holder.timetsmap.setText(entry.getTimestamp());
        holder.title.setText(entry.getTitle());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}
