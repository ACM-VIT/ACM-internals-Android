package com.acmvit.acm_app.ui.projects.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.ItemPaginationStatusBinding;
import com.acmvit.acm_app.databinding.ItemProjectBinding;
import com.acmvit.acm_app.model.Project;
import com.acmvit.acm_app.model.User;
import com.acmvit.acm_app.ui.custom.OnItemClickListener;
import com.acmvit.acm_app.ui.custom.OverlapItemDecorator;
import com.acmvit.acm_app.ui.custom.SimpleDiffCallback;
import com.acmvit.acm_app.util.PaginatedResource;
import com.acmvit.acm_app.util.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectsRvAdapter
    extends PagedListAdapter<Project, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CONTENT = 0;
    private static final int VIEW_TYPE_STATUS_FOOTER = 1;

    private static final String TAG = "ProjectsRvAdapter";
    private int totalCount = 0;
    private OnItemClickListener onItemClickListener = pos -> {};
    private PaginatedResource<Project> paginatedResource;
    private final LifecycleOwner lifecycleOwner;

    public ProjectsRvAdapter(
        PaginatedResource<Project> paginatedResource,
        LifecycleOwner lifecycleOwner
    ) {
        super(
            new SimpleDiffCallback<Project>() {
                @Override
                public Object getKeyProperty(Project project) {
                    return project.getProject_id();
                }
            }
        );
        this.lifecycleOwner = lifecycleOwner;
        setPaginatedResource(paginatedResource);
    }

    public void setPaginatedResource(
        @NonNull PaginatedResource<Project> paginatedResource
    ) {
        paginatedResource.data.observe(
            lifecycleOwner,
            d -> {
                submitList(d);
                if (d == null) {
                    notifyItemRemoved(super.getItemCount());
                    return;
                }
                d.addWeakCallback(
                    new ArrayList<>(),
                    new PagedList.Callback() {
                        @Override
                        public void onChanged(int position, int count) {}

                        @Override
                        public void onInserted(int position, int count) {
                            if (totalCount == 0) {
                                Log.d(TAG, "onInserted: callback");
                                notifyItemInserted(
                                    ProjectsRvAdapter.super.getItemCount()
                                );
                            }
                            totalCount = ProjectsRvAdapter.super.getItemCount();
                        }

                        @Override
                        public void onRemoved(int position, int count) {
                            if (
                                ProjectsRvAdapter.super.getItemCount() == 0 &&
                                totalCount != 0
                            ) {
                                Log.d(TAG, "onRemoved: callback");
                                notifyItemRemoved(
                                    ProjectsRvAdapter.super.getItemCount()
                                );
                            }
                            totalCount = ProjectsRvAdapter.super.getItemCount();
                        }
                    }
                );
            }
        );
        paginatedResource.status.observe(
            lifecycleOwner,
            status -> {
                if (canEnableStatusView()) {
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        );
        this.paginatedResource = paginatedResource;
    }

    public void setOnItemClickListener(
        OnItemClickListener onItemClickListener
    ) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent,
        int viewType
    ) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CONTENT) {
            return new ProjectsViewHolder(
                ItemProjectBinding.inflate(inflater, parent, false)
            );
        } else {
            return new LoadingStatusViewHolder(
                ItemPaginationStatusBinding.inflate(inflater, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(
        @NonNull RecyclerView.ViewHolder holder,
        int position
    ) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (holder instanceof ProjectsViewHolder) {
            ((ProjectsViewHolder) holder).bind(getItem(position));
        } else if (holder instanceof LoadingStatusViewHolder) {
            ((LoadingStatusViewHolder) holder).bind(
                    paginatedResource.status.getValue(),
                    paginatedResource.requestAgain
                );
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == super.getItemCount()) {
            return VIEW_TYPE_STATUS_FOOTER;
        }
        return VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return canEnableStatusView()
            ? super.getItemCount() + 1
            : super.getItemCount();
    }

    private boolean canEnableStatusView() {
        return (
            super.getItemCount() > 0 &&
            paginatedResource.data.getValue() != null
        );
    }

    public class ProjectsViewHolder extends RecyclerView.ViewHolder {

        private final ItemProjectBinding binding;

        public ProjectsViewHolder(ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding
                .getRoot()
                .setOnClickListener(
                    v -> onItemClickListener.onItemClick(getAdapterPosition())
                );
            binding.projectMembersRv.setAdapter(
                new PeopleRvAdapter(new ArrayList<>())
            );
            binding.projectTagsRv.setAdapter(
                new TagsRvAdapter(new ArrayList<>(), R.color.colorSurface)
            );
            binding.projectMembersRv.addItemDecoration(
                new OverlapItemDecorator(LinearLayoutManager.HORIZONTAL, 10)
            );
        }

        public void bind(Project project) {
            if (project == null) return;

            binding.setProject(project);
            List<User> users = project.getMembersWithFounder();
            List<String> tags = project.getTags();

            if (users != null) {
                (
                    (PeopleRvAdapter) binding.projectMembersRv.getAdapter()
                ).setUsers(users);
            }

            if (tags != null) {
                ((TagsRvAdapter) binding.projectTagsRv.getAdapter()).setTags(
                        tags
                    );
            }
        }
    }

    public static class LoadingStatusViewHolder
        extends RecyclerView.ViewHolder {

        private final ItemPaginationStatusBinding binding;

        public LoadingStatusViewHolder(ItemPaginationStatusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Status status, Runnable retryCallback) {
            binding.setRetryCallback(retryCallback);
            binding.setStatus(status);
            binding.executePendingBindings();
        }
    }
}
