package com.acmvit.acm_app.ui.invites.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RequestsSwipeHelper extends ItemTouchHelper.SimpleCallback {

    private final RequestsRvAdapter adapter;

    public RequestsSwipeHelper(RequestsRvAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(
        @NonNull RecyclerView recyclerView,
        @NonNull RecyclerView.ViewHolder viewHolder,
        @NonNull RecyclerView.ViewHolder target
    ) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);
    }
}
