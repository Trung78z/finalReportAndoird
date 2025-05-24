package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.management.R;
import com.hcmus.management.adapter.AdapterOrder;
import com.hcmus.management.dto.PaymentDTO;
import com.hcmus.management.dto.TransactionItemsDTO;
import com.hcmus.management.model.FoodItem;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.CartRequest;
import com.hcmus.management.network.VolleySingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityPayment extends AppCompatActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_payment);
		findViewById(R.id.btnBack).setOnClickListener(v -> {
			finish();
		});
		
		Intent intent = getIntent();
		String total = intent.getStringExtra("total");
		TextView tvTotal = findViewById(R.id.tvTotal);
		tvTotal.setText("$ " + total);
		ListView listView = findViewById(R.id.lvOrder);
		List<FoodItem> foodItemList = (List<FoodItem>) intent.getSerializableExtra("foodItemList");
		if (foodItemList.size() > 1) {
			float scale = getResources().getDisplayMetrics().density;

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = 160 * (int)scale;
			listView.setLayoutParams(params);
		}
		AdapterOrder adapter = new AdapterOrder(this, foodItemList);
		listView.setAdapter(adapter);
		findViewById(R.id.btn_checkout).setOnClickListener(v -> {
			PaymentDTO paymentDTO = new PaymentDTO();
			paymentDTO.setTotalPrice(total);
			List<TransactionItemsDTO> transactionItemsDTOList = foodItemList.stream()
					.map(item -> new TransactionItemsDTO().setCartId(item.getCartId()))
					.collect(Collectors.toList());
			paymentDTO.setTransactionItems(transactionItemsDTOList);
			CartRequest.payment(
					ActivityPayment.this,
					VolleySingleton.getInstance(ActivityPayment.this).getRequestQueue(),
					paymentDTO,
					new CartRequest.Callback() {
						@Override
						public void onSuccess(org.json.JSONObject response) {
							android.widget.Toast.makeText(ActivityPayment.this, "Payment success!", android.widget.Toast.LENGTH_SHORT).show();
							setResult(RESULT_OK, intent);
							finish();
						}
						
						@Override
						public void onError(String message) {
							android.widget.Toast.makeText(ActivityPayment.this, "Payment failed: " + message, android.widget.Toast.LENGTH_SHORT).show();
						}
					}
			);
		});
	}
}
