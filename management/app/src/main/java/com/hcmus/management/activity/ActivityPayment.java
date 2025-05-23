package com.hcmus.management.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.management.R;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.CartRequest;
import com.hcmus.management.network.VolleySingleton;

public class ActivityPayment extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
        });
        
        String total = getIntent().getStringExtra("total");
        // Now you can use 'total', for example:
         TextView tvTotal = findViewById(R.id.tvTotal);
         tvTotal.setText("$ " +total);
         findViewById(R.id.btn_checkout).setOnClickListener(v -> {
                 CartRequest.payment(
                         ActivityPayment.this,
                         VolleySingleton.getInstance(ActivityPayment.this).getRequestQueue(),
                         Double.parseDouble(total),
                         new AuthRequest.Callback() {
                             @Override
                             public void onSuccess(org.json.JSONObject response) {
                                 android.widget.Toast.makeText(ActivityPayment.this, "Payment success!", android.widget.Toast.LENGTH_SHORT).show();
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
