package com.acmvit.acm_app.ui.members;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.DialogBottomMembersBinding;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.acmvit.acm_app.ui.base.BaseViewModelFactory;
import com.acmvit.acm_app.ui.members.adapters.MembersBottomAdapter;
import com.acmvit.acm_app.ui.projects.ProjectsViewModel;
import com.acmvit.acm_app.util.GeneralUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MembersBottomDialogFragment extends BottomSheetDialogFragment {

    public static final String RESULT_KEY = "USER_SELECTED";
    private DialogBottomMembersBinding binding;
    private NavController navController;
    private MembersBottomViewModel viewModel;
    private MembersBottomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState
    ) {
        binding =
            DialogBottomMembersBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = NavHostFragment.findNavController(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
        @NonNull View view,
        @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        viewModel =
            new ViewModelProvider(
                this,
                new BaseViewModelFactory((BaseActivity) getActivity())
            )
                .get(MembersBottomViewModel.class);
        binding.setViewmodel(viewModel);
        binding.searchBar.clearFocus();

        initRv();
    }

    private void initRv() {
        adapter = new MembersBottomAdapter();
        viewModel
            .getFilteredUsers()
            .observe(getViewLifecycleOwner(), adapter::submitList);
        adapter.setOnItemClickListener(
            pos -> {
                navController
                    .getPreviousBackStackEntry()
                    .getSavedStateHandle()
                    .set(RESULT_KEY, adapter.getCurrentList().get(pos));
                GeneralUtils.hideKeyboard(getContext(), binding.searchBar);
                dismissAllowingStateLoss();
            }
        );
        adapter.registerAdapterDataObserver(
            new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(
                    int positionStart,
                    int itemCount
                ) {
                    int firstVisiblePos =
                        (
                            (LinearLayoutManager) binding.membersRv.getLayoutManager()
                        ).findFirstCompletelyVisibleItemPosition();
                    if (positionStart < firstVisiblePos) {
                        binding.membersRv.smoothScrollToPosition(0);
                    }
                }
            }
        );

        binding.membersRv.setLayoutManager(
            new LinearLayoutManager(getContext())
        );
        binding.membersRv.setAdapter(adapter);
    }
}
