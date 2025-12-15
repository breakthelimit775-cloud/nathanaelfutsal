package com.example.nathanaelfutsal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity implements JadwalAdapter.OnJadwalClickListener {

    private RecyclerView rvJadwal;
    private TextView tvJamTerpilih, tvTotalHarga;
    private Button btnPilihJam;
    private DBHelper dbHelper;
    private JadwalAdapter adapter;
    private ArrayList<JadwalModel> jadwalList;
    private final ArrayList<String> jamTerpilihList = new ArrayList<>();
    private static final int HARGA_PER_JAM = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DBHelper(this);
        rvJadwal = findViewById(R.id.rvJadwal);
        tvJamTerpilih = findViewById(R.id.tvJamTerpilih);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        btnPilihJam = findViewById(R.id.btnPilihJam);

        loadJadwal();
        updateSummary();

        btnPilihJam.setOnClickListener(v -> {
            if (jamTerpilihList.isEmpty()) {
                Toast.makeText(this, "Silakan pilih jam terlebih dahulu.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(BookingActivity.this, FormActivity.class);
            intent.putStringArrayListExtra("JAM_BOOKING_LIST", jamTerpilihList);
            intent.putExtra("TOTAL_HARGA", jamTerpilihList.size() * HARGA_PER_JAM);
            intent.putExtra("NAMA_LAPANGAN", "Lapangan 2 (Vinyl Pro)");
            startActivity(intent);
        });
    }

    private void loadJadwal() {
        jadwalList = dbHelper.getAllSchedules();
        adapter = new JadwalAdapter(this, jadwalList, this);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
        rvJadwal.setAdapter(adapter);
    }

    private void updateSummary() {
        if (jamTerpilihList.isEmpty()) {
            tvJamTerpilih.setText("Jam Terpilih: (Tidak ada)");
            tvTotalHarga.setText("Total Harga: Rp 0");
            btnPilihJam.setText("Pilih Jam");
            btnPilihJam.setBackgroundColor(getResources().getColor(R.color.purple_500));
            btnPilihJam.setEnabled(true);
        } else {
            int total = jamTerpilihList.size() * HARGA_PER_JAM;
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            tvJamTerpilih.setText("Jam Terpilih: " + String.join(", ", jamTerpilihList));
            tvTotalHarga.setText("Total Harga: " + formatter.format(total));

            btnPilihJam.setText("Lanjutkan ke Pembayaran");
            btnPilihJam.setBackgroundColor(getResources().getColor(R.color.green));
            btnPilihJam.setEnabled(true);
        }
    }

    @Override
    public void onJadwalClick(String jam, boolean isSelected) {
        if (isSelected) {
            if (!jamTerpilihList.contains(jam)) {
                jamTerpilihList.add(jam);
            }
        } else {
            jamTerpilihList.remove(jam);
        }
        updateSummary();
    }
}