package com.acmvit.acm_app.ui.projects.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.acmvit.acm_app.R;
import com.acmvit.acm_app.databinding.ItemChipBinding;
import com.acmvit.acm_app.ui.custom.OnItemClickListener;
import com.google.android.gms.common.util.Strings;
import java.util.List;

public class TagsRvAdapter extends RecyclerView.Adapter<TagsRvAdapter.TagsVH> {

    private static final String TAG = "TagsRvAdapter";
    private final SortedList<String> tags;
    private OnItemClickListener onItemClickListener = pos -> {};
    private final Integer bgColor;

    public TagsRvAdapter(List<String> tags, Integer bgColorResId) {
        this(bgColorResId);
        this.tags.addAll(tags);
    }

    public TagsRvAdapter(Integer bgColorResId) {
        this.tags =
            new SortedList<>(
                String.class,
                new SortedListAdapterCallback<String>(this) {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }

                    @Override
                    public boolean areContentsTheSame(
                        String oldItem,
                        String newItem
                    ) {
                        return true;
                    }

                    @Override
                    public boolean areItemsTheSame(String item1, String item2) {
                        return item1.equals(item2);
                    }
                }
            );
        this.bgColor = bgColorResId;
    }

    public void setOnItemClickListener(
        OnItemClickListener onItemClickListener
    ) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setTags(List<String> tags) {
        Log.d(TAG, "setTags: " + tags);
        this.tags.replaceAll(tags);
        notifyDataSetChanged();
    }

    public SortedList<String> getTags() {
        return tags;
    }

    @NonNull
    @Override
    public TagsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TagsVH(ItemChipBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagsVH holder, int position) {
        holder.bind(tags.get(position));
    }

    @Override
    public int getItemCount() {
        if (tags == null) {
            return 0;
        }
        return tags.size();
    }

    public class TagsVH extends RecyclerView.ViewHolder {

        ItemChipBinding binding;

        public TagsVH(ItemChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setBackgroundResId(bgColor);
            binding.tagChip.setOnClickListener(
                v -> onItemClickListener.onItemClick(getAdapterPosition())
            );
            binding.executePendingBindings();
        }

        public void bind(String tag) {
            binding.setText(tag);
        }
    }
}
