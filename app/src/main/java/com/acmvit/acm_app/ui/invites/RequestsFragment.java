package com.acmvit.acm_app.ui.invites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.acmvit.acm_app.databinding.FragmentRequestsBinding;
import com.acmvit.acm_app.model.Request;
import com.acmvit.acm_app.ui.invites.adapters.RequestsRvAdapter;
import com.acmvit.acm_app.ui.invites.adapters.RequestsSwipeHelper;
import com.acmvit.acm_app.util.MockData;
import java.util.List;

public class RequestsFragment extends Fragment {

    private FragmentRequestsBinding binding;
    private List<Request> requests;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        requests = MockData.requests;
        initRv();
    }

    private void initRv() {
        RequestsRvAdapter adapter = new RequestsRvAdapter(requests);
        binding.requestsRv.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
            new RequestsSwipeHelper(adapter)
        );
        itemTouchHelper.attachToRecyclerView(binding.requestsRv);
    }
}
