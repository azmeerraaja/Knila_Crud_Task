package com.azmeer.knilacrud.view.ui.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.azmeer.knilacrud.R;
import com.azmeer.knilacrud.databinding.ItemDashboardBinding;
import com.azmeer.knilacrud.models.UserModel;

import java.util.List;


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {

    private List<UserModel> userModels;
    private LayoutInflater layoutInflater;
    private DashboardAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ItemDashboardBinding binding;

        public MyViewHolder(final ItemDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public DashboardAdapter(List<UserModel> userList, DashboardAdapterListener listener) {
        this.userModels = userList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemDashboardBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_dashboard, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setItem(userModels.get(position));
        holder.binding.ivMore.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(userModels.get(position),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public interface DashboardAdapterListener {
        void onItemClicked(UserModel userModel, View v);
    }
}
