package com.acmvit.acm_app.ui.projects.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;
import com.acmvit.acm_app.databinding.ItemPeopleInBinding;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.ui.custom.OnItemClickListener;
import java.util.List;

public class PeopleRvAdapter
    extends RecyclerView.Adapter<PeopleRvAdapter.PeopleVH> {

    private static final String TAG = "PeopleRvAdapter";

    private List<User> users;
    private OnItemClickListener onItemClickListener = pos -> {};

    public PeopleRvAdapter(List<User> users) {
        this.users = users;
    }

    public void setOnItemClickListener(
        OnItemClickListener onItemClickListener
    ) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PeopleVH onCreateViewHolder(
        @NonNull ViewGroup parent,
        int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PeopleVH(
            ItemPeopleInBinding.inflate(inflater, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleVH holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class PeopleVH extends RecyclerView.ViewHolder {

        ItemPeopleInBinding binding;

        public PeopleVH(@NonNull ItemPeopleInBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding
                .getRoot()
                .setOnClickListener(
                    v -> onItemClickListener.onItemClick(getAdapterPosition())
                );
        }

        public void bind(User user) {
            if (user == null) {
                return;
            }
            binding.setImg(user.getDp());
            binding.setIsPrimary(getAdapterPosition() == 0);
            binding.executePendingBindings();
        }
    }
}
