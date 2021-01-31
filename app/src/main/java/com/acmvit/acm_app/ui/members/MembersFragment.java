package com.acmvit.acm_app.ui.members;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.acmvit.acm_app.MembersAdapter;
import com.acmvit.acm_app.databinding.FragmentMembersBinding;
import org.jetbrains.annotations.NotNull;

public class MembersFragment extends Fragment {

    private MembersViewModel viewModel;
    MembersAdapter adapter;
    private FragmentMembersBinding binding;

    @Override
    public View onCreateView(
        @NotNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        binding = FragmentMembersBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.searchBar.setOnQueryTextListener(
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    viewModel.filterUsers(newText.toLowerCase());
                    return false;
                }
            }
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        viewModel = new ViewModelProvider(this).get(MembersViewModel.class);
        adapter = new MembersAdapter(viewModel.list.getValue());
        binding.recyclerView.setAdapter(adapter);
        viewModel.list.observe(
            getViewLifecycleOwner(),
            userArrayList -> {
                adapter.replace(userArrayList);
                adapter.notifyDataSetChanged();
            }
        );
        viewModel.allUsers.observe(
            getViewLifecycleOwner(),
            allUserList -> viewModel.list.postValue(allUserList)
        );
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
