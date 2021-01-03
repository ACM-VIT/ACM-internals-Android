package com.acmvit.acm_app.ui.invites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.acmvit.acm_app.databinding.ItemNotificationBinding;
import com.acmvit.acm_app.model.Notification;
import java.util.List;

public class NotificationsRvAdapter
    extends RecyclerView.Adapter<NotificationsRvAdapter.NotificationRvVH> {

    private final List<Notification> notifications;

    public NotificationsRvAdapter(@NonNull List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationRvVH onCreateViewHolder(
        @NonNull ViewGroup parent,
        int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(
            inflater,
            parent,
            false
        );

        return new NotificationRvVH(binding);
    }

    @Override
    public void onBindViewHolder(
        @NonNull NotificationRvVH holder,
        int position
    ) {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationRvVH extends RecyclerView.ViewHolder {

        private final ItemNotificationBinding binding;

        public NotificationRvVH(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Notification notification) {
            binding.setNotification(notification);
        }
    }
}
