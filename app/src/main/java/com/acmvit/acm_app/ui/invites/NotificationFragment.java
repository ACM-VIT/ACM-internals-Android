package com.acmvit.acm_app.ui.invites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.acmvit.acm_app.databinding.FragmentNotificationsBinding;
import com.acmvit.acm_app.model.Notification;
import com.acmvit.acm_app.ui.invites.adapters.NotificationsRvAdapter;
import com.acmvit.acm_app.util.MockData;
import java.util.List;

public class NotificationFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<Notification> notifications;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding =
            FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        notifications = MockData.notifications;
        initRv();
    }

    private void initRv() {
        NotificationsRvAdapter adapter = new NotificationsRvAdapter(
            notifications
        );
        binding.notificationsRv.setAdapter(adapter);
    }
}
