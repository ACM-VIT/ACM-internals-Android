package com.acmvit.acm_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acmvit.acm_app.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    ArrayList<User> users;
    public MembersAdapter(ArrayList<User> userArrayList){
        users=userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        Picasso.with(holder.itemView.getContext())
                .load(users.get(position).getDp())
                .placeholder(R.drawable.ic_worker)
                .into(holder.dp);

    }

    public void replace(ArrayList<User> newList){
        this.users=newList;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView dp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.member_name);
            dp=itemView.findViewById(R.id.member_dp);

        }
    }
}


