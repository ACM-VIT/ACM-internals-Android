package com.acmvit.acm_app.ui.members.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.acmvit.acm_app.databinding.DialogBottomMembersBinding;
import com.acmvit.acm_app.databinding.ItemDialogMemberBinding;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.ui.custom.OnItemClickListener;
import com.acmvit.acm_app.ui.custom.SimpleDiffCallback;

public class MembersBottomAdapter extends ListAdapter<User, MembersBottomAdapter.MembersVH> {

    private OnItemClickListener onItemClickListener = pos -> {};

    public MembersBottomAdapter() {
        super(new SimpleDiffCallback<User>() {
            @Override
            public Object getKeyProperty(User user) {
                return user.getUser_id();
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MembersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDialogMemberBinding binding = ItemDialogMemberBinding.inflate(inflater, parent, false);
        return new MembersVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersVH holder, int position) {
        holder.bind(getItem(position));
    }

    public class MembersVH extends RecyclerView.ViewHolder {
        private final ItemDialogMemberBinding binding;

        public MembersVH(ItemDialogMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
        }

        public void bind(User user) {
            binding.setUser(user);
        }

    }
}
