package com.hcmus.management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcmus.management.R;
import com.hcmus.management.model.FoodItem;

import java.util.List;

public class AdapterOrder extends BaseAdapter {
	
	private Context context;
	private List<FoodItem> items;
	
	public AdapterOrder(Context context, List<FoodItem> items) {
		this.context = context;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}
	
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	static class ViewHolder {
		ImageView ivFood;
		TextView tvFoodName, tvFoodPrice, tvQuantity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.cart_order, parent, false);
			holder = new ViewHolder();
			holder.ivFood = convertView.findViewById(R.id.ivFood);
			holder.tvFoodName = convertView.findViewById(R.id.tvFoodName);
			holder.tvFoodPrice = convertView.findViewById(R.id.tvFoodPrice);
			holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		FoodItem item = items.get(position);
		
		Glide.with(context)
				.load(item.getImageUrl())
				.placeholder(R.drawable.ic_placeholder)
				.error(R.drawable.ic_error)
				.into(holder.ivFood);
		
		holder.tvFoodName.setText(item.getName());
		holder.tvFoodPrice.setText(String.format("$ %.2f", item.getPrice()));
		holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
		
		return convertView;
	}
}