package com.secretbiology.ncbsmod.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secretbiology.ncbsmod.R;
import com.secretbiology.ncbsmod.models.ExternalDataModel;

import java.util.List;

public class UserListAdaper extends RecyclerView.Adapter<UserListAdaper.MyViewHolder> {

    private List<ExternalDataModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, email;
        LinearLayout fullLayout;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            name = (TextView) view.findViewById(R.id.user_name);
            email = (TextView) view.findViewById(R.id.user_email);
            fullLayout = (LinearLayout) view.findViewById(R.id.user_tem_layout);
            fullLayout.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public UserListAdaper(List<ExternalDataModel> entryList) {
        this.entryList = entryList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ExternalDataModel entry = entryList.get(position);
        holder.name.setText(entry.getName());
        holder.email.setText(entry.getEmail() );
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


}
