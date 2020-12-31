package com.acmvit.acm_app.ui.invites.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acmvit.acm_app.databinding.ItemRequestsBinding;
import com.acmvit.acm_app.model.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestsRvAdapter extends RecyclerView.Adapter<RequestsRvAdapter.RequestRvVH> {
    private final List<Request> requests;

    public RequestsRvAdapter(@NonNull List<Request> requests) {
        this.requests = new ArrayList<>(requests);
    }

    @NonNull
    @Override
    public RequestRvVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRequestsBinding binding = ItemRequestsBinding
                .inflate(inflater, parent, false);

        return new RequestRvVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestRvVH holder, int position) {
        holder.bind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void deleteItem(int position) {
        requests.remove(position);
        notifyItemRemoved(position);
    }

    public static class RequestRvVH extends RecyclerView.ViewHolder {
        private final ItemRequestsBinding binding;

        public RequestRvVH(ItemRequestsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Request request){
            binding.setRequest(request);
        }
    }
}
