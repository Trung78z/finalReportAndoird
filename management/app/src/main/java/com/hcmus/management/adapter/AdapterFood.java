package com.hcmus.management.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hcmus.management.R;
import com.hcmus.management.model.FoodItem;
import com.hcmus.management.network.FoodRequest;

import org.json.JSONObject;

import java.util.List;

import lombok.NonNull;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<FoodItem> items;

    public CartAdapter(List<FoodItem> items) {
        this.items = items;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivChecked, ivFood;
        TextView tvFoodName, tvFoodPrice, tvQuantity;
        ImageButton btnPlus, btnMinus, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivChecked = itemView.findViewById(R.id.ivChecked);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        FoodItem item = items.get(position);

        // Load image from URL using Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_placeholder) // Use your placeholder drawable
                .error(R.drawable.ic_error) // Use your error drawable
                .into(holder.ivFood);

        holder.tvFoodName.setText(item.getName());
        holder.tvFoodPrice.setText(String.format("$ %.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Call backend delete
            FoodRequest.deleteFood(holder.itemView.getContext(), item.getId(), new FoodRequest.Callback() {
                @Override
                public void onSuccess(JSONObject response) {
                    // Remove from list and update UI
                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size());
                }

                @Override
                public void onError(String message) {
                    Log.e("CartAdapter", "Delete failed: " + message);
                    Toast.makeText(holder.itemView.getContext(), "Delete failed: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FoodItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }
}
