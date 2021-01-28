package com.acmvit.acm_app.ui.ideas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.acmvit.acm_app.R;
import java.util.ArrayList;

public class DashboardIdeasAdapter
    extends RecyclerView.Adapter<DashboardIdeasAdapter.ViewHolder> {

    private ArrayList<Idea> items;

    public DashboardIdeasAdapter(ArrayList<Idea> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
            .from(viewGroup.getContext())
            .inflate(R.layout.list_item_dashboard_rv, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.img.setImageResource(items.get(position).getImgResource());
        holder.date.setText(items.get(position).getDate());
        holder.likes.setText(
            Integer.toString(items.get(position).getLikes_count())
        );
        holder.topic.setText(items.get(position).getTopicName());
        holder.description.setText(items.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView name = itemView.findViewById(R.id.dashboard_list_name);
        ImageView img = itemView.findViewById(R.id.dashboard_list_profile_pic);
        TextView date = itemView.findViewById(R.id.dashboard_list_date);
        TextView likes = itemView.findViewById(R.id.dashboard_list_likes);
        TextView topic = itemView.findViewById(R.id.dashboard_list_topic_name);
        TextView description = itemView.findViewById(
            R.id.dashboard_list_description
        );
    }
}
