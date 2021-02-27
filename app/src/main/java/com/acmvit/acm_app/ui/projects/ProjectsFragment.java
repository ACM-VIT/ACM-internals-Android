package com.acmvit.acm_app.ui.projects;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.FragmentProjectsBinding;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.ui.base.BaseActivity;
import com.acmvit.acm_app.ui.base.BaseViewModelFactory;
import com.acmvit.acm_app.ui.custom.MLTransitionListener;
import com.acmvit.acm_app.ui.members.MembersBottomDialogFragment;
import com.acmvit.acm_app.ui.projects.adapters.ProjectsRvAdapter;
import com.acmvit.acm_app.ui.projects.adapters.TagsRvAdapter;
import com.acmvit.acm_app.util.GeneralUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.chip.ChipDrawable;

import org.jetbrains.annotations.NotNull;

public class ProjectsFragment extends Fragment {
    private static final String TAG = "ProjectsFragment";

    private FragmentProjectsBinding binding;
    private ProjectsViewModel viewModel;
    private NavController navController;
    private SavedStateHandle savedStateHandle;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        navController = NavHostFragment.findNavController(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new BaseViewModelFactory((BaseActivity) getActivity())).get(ProjectsViewModel.class);
        binding.setViewmodel(viewModel);
        savedStateHandle = navController.getBackStackEntry(R.id.projectsFragment).getSavedStateHandle();
        MutableLiveData<User> userFilterSelected = savedStateHandle.getLiveData(MembersBottomDialogFragment.RESULT_KEY);
        userFilterSelected.observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            viewModel.getFilters().setUser(user);
            userFilterSelected.setValue(null);
        });
        initMyChip();
        initObservers();
        initAnimations();
        initProjectsRv(binding.projectsRv);
        initTagsRv(binding.tagsRv);
    }

    private void initObservers() {
        viewModel.getIsSearchExpanded().observe(getViewLifecycleOwner(), this::toggleSearchUI);
        viewModel.getFilters().getUser().observe(getViewLifecycleOwner(), user -> binding.myChip.setChecked(user != null));
        viewModel.getFilters().getTagSearchFilter().observe(getViewLifecycleOwner(), tagSearchFilter -> {
            if (viewModel.getFilters().getIsTag()) {
                binding.searchTextfield.setTextSize(24);
                setSpan();
            } else {
                removeSpans();
            }
        });
        viewModel.getScrollToTop().observe(getViewLifecycleOwner(), aVoid -> binding.projectsRv.scrollToPosition(0));

    }

    private void initMyChip() {
        binding.myChip.setOnClickListener(v ->
                navController.navigate(ProjectsFragmentDirections.actionProjectsFragmentToMembersBottomDialogFragment()));
        binding.myChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean viewModelChecked = viewModel.getFilters().getUser().getValue() != null;
            if (viewModelChecked != isChecked) {
                buttonView.setChecked(viewModelChecked);
            } else {
                binding.myChip.setChipIconSizeResource(isChecked ? R.dimen.my_chip_selected_icon_size : R.dimen.my_chip_unselected_icon_size);
                binding.myChip.setTextEndPaddingResource(isChecked ? R.dimen.my_chip_selected_text_end_padding : R.dimen.my_chip_unselected_text_end_padding);
                binding.myChip.setCloseIconVisible(isChecked);
                if (isChecked) {
                    binding.myChip.setChipIconTint(null);
                } else {
                    binding.myChip.setChipIconTintResource(R.color.colorPrimaryDark);
                }
            }
        });
    }

    private void initTagsRv(RecyclerView rv) {
        TagsRvAdapter adapter = new TagsRvAdapter(R.color.colorSurfaceLight);
        viewModel.getTags().observe(getViewLifecycleOwner(), adapter::setTags);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(pos -> {
            viewModel.submitSpan(adapter.getTags().get(pos));
            Log.d(TAG, "initTagsRv: " + adapter.getTags());
        });
        rv.setAdapter(adapter);
    }

    private void initProjectsRv(RecyclerView rv) {
        rv.setItemViewCacheSize(5);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setItemPrefetchEnabled(true);
        linearLayoutManager.setInitialPrefetchItemCount(5);
        ProjectsRvAdapter projectsAdapter = new ProjectsRvAdapter(viewModel.getProjectsPaginatedResource(), getViewLifecycleOwner());
        projectsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeInserted: " + positionStart + " " + itemCount + " " + projectsAdapter.getItemCount());
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                Log.d(TAG, "onItemRangeInserted: " + fromPosition + " " + itemCount + " " + projectsAdapter.getItemCount());

            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeInserted: " + positionStart + " " + itemCount + " " + projectsAdapter.getItemCount());
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeInserted: " + positionStart + " " + itemCount + " " + projectsAdapter.getItemCount());
                if (positionStart == 0) {
                    Log.d(TAG, "zeroinserted: ");
//                    rv.scrollToPosition(0);
                }


            }
        });
        rv.setAdapter(projectsAdapter);
    }

    private void initAnimations() {
        binding.searchTextfield.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                viewModel.setSearchViewExpand(true);
            }
        });
        binding.rootMl.setTransition(R.id.scroll_collapse_ts);
        binding.rootMl.setTransitionListener(new MLTransitionListener() {
            final float overlay_unfocused_elevation = getResources().getDimension(R.dimen.overlay_elevation_unfocused);

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int current) {
                if (current == R.id.start && binding.overlayView.getElevation() != overlay_unfocused_elevation) {
                    binding.overlayView.setElevation(overlay_unfocused_elevation);
                    GeneralUtils.hideKeyboard(getContext(), binding.searchTextfield);
                    binding.rootMl.setTransition(R.id.scroll_collapse_ts);
                }
            }
        });
    }

    private void toggleSearchUI(boolean enable) {
        boolean isCurrentEnable = binding.rootMl.getCurrentState() == R.id.end_search_extend;
        if (isCurrentEnable == enable) return;

        binding.rootMl.setTransition(R.id.search_extend_ts);
        if (enable) {
            binding.overlayView.setElevation(getResources().getDimension(R.dimen.overlay_elevation_focus));
            binding.rootMl.transitionToEnd();
        } else {
            binding.searchTextfield.clearFocus();
            binding.rootMl.transitionToStart();
        }
    }

    private void setSpan() {
        ChipDrawable chip = ChipDrawable.createFromResource(getContext(), R.xml.standalone_chip);
        EditText search = binding.searchTextfield;
        binding.executePendingBindings();
        chip.setText(viewModel.getSearchData().getValue());
        chip.setBounds(0, 0, chip.getIntrinsicWidth(), search.getHeight() - search.getTotalPaddingTop() - search.getTotalPaddingBottom());
        ImageSpan span = new ImageSpan(chip);
        Editable editable = search.getText();
        editable.setSpan(span, 0, viewModel.getSearchData().getValue().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    private void removeSpans() {
        Editable text = binding.searchTextfield.getText();
        ImageSpan[] toRemoveSpans = text.getSpans(0, text.length(), ImageSpan.class);
        for (ImageSpan toRemoveSpan : toRemoveSpans) text.removeSpan(toRemoveSpan);
    }

}