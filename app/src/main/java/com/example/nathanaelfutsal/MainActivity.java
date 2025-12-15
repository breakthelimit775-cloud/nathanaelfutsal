package com.example.nathanaelfutsal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LapanganAdapter adapter;
    ArrayList<LapanganModel> listLapangan;

    ImageView btnProfile;
    TextView tvGreetingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnProfile = findViewById(R.id.btnProfile);
        tvGreetingName = findViewById(R.id.tvGreetingName);
        recyclerView = findViewById(R.id.recyclerViewLapang);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        setupGreeting();
        listLapangan = new ArrayList<>();
        isiDataDummy();

        adapter = new LapanganAdapter(this, listLapangan);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupGreeting() {
        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        String nama = pref.getString("name", "Player"); // Default "Player" jika kosong
        Calendar c = Calendar.getInstance();
        int jam = c.get(Calendar.HOUR_OF_DAY);
        String sapaanWaktu;

        if (jam >= 4 && jam < 11) {
            sapaanWaktu = "Selamat Pagi";
        } else if (jam >= 11 && jam < 15) {
            sapaanWaktu = "Selamat Siang";
        } else if (jam >= 15 && jam < 18) {
            sapaanWaktu = "Selamat Sore";
        } else {
            sapaanWaktu = "Selamat Malam";
        }
        tvGreetingName.setText(sapaanWaktu + ",\n" + nama);
    }
    private void isiDataDummy() {
        listLapangan.clear();

        for (int i = 1; i <= 20; i++) {

            String namaLapangan;
            String harga;
            String status;
            int gambarLapangan;

            if (i % 3 == 1) {
                namaLapangan = "Lapangan " + i + " (Sintetis)";
                harga = "Rp 150.000 / jam";
                status = "Tersedia";
                gambarLapangan = R.drawable.img_sintetis;

            } else if (i % 3 == 2) {
                namaLapangan = "Lapangan " + i + " (Vinyl Pro)";
                harga = "Rp 180.000 / jam";
                status = "Tersedia";
                gambarLapangan = R.drawable.img_vinyl;

            } else {
                namaLapangan = "Lapangan " + i + " (Outdoor)";
                harga = "Rp 100.000 / jam";
                status = (i % 2 == 0) ? "Penuh" : "Tersedia";
                gambarLapangan = R.drawable.img_semen;
            }

            listLapangan.add(new LapanganModel(
                    namaLapangan,
                    harga,
                    status,
                    gambarLapangan
            ));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setupGreeting();
    }
}