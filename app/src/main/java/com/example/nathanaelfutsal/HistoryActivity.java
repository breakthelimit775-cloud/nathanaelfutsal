package com.example.nathanaelfutsal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView lvHistory;
    private TextView tvEmptyHistory;
    private ImageView btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DBHelper(this);
        lvHistory = findViewById(R.id.lvHistory);
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory);
        btnKembali = findViewById(R.id.btnKembaliHistory);

        btnKembali.setOnClickListener(v -> finish());

        loadHistoryData();
    }

    private void loadHistoryData() {
        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        String userEmail = pref.getString("email", null);

        if (userEmail == null || userEmail.isEmpty()) {
            tvEmptyHistory.setText("Gagal memuat riwayat. User tidak teridentifikasi.");
            tvEmptyHistory.setVisibility(View.VISIBLE);
            return;
        }

        ArrayList<JadwalModel> historyList = dbHelper.getHistoryTransaksi(userEmail);

        if (historyList.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            lvHistory.setVisibility(View.GONE);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            lvHistory.setVisibility(View.VISIBLE);

            HistoryAdapter adapter = new HistoryAdapter(this, historyList);
            lvHistory.setAdapter(adapter);
        }
    }
}