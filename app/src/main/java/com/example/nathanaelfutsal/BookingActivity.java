package com.example.nathanaelfutsal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity implements JadwalAdapter.OnSelectionChangedListener {
    TextView tvNamaLapangan, tvTanggal, tvJamTerpilih, tvTotalHarga;
    ImageView btnKembali;
    RecyclerView rvJadwal;
    Button btnPesan;
    private DBHelper dbHelper;
    private static final String TAG = "BookingActivity";
    JadwalAdapter adapter;
    ArrayList<JadwalModel> listJadwal;
    ArrayList<JadwalModel> slotTerpilih = new ArrayList<>();
    String namaLapanganDiterima;
    int hargaPerJam = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        dbHelper = new DBHelper(this);
        namaLapanganDiterima = getIntent().getStringExtra("NAMA_LAPANGAN");
        tvNamaLapangan = findViewById(R.id.tvNamaLapanganBooking);
        tvTanggal = findViewById(R.id.tvTanggalBooking);
        btnKembali = findViewById(R.id.btnKembaliBooking);
        rvJadwal = findViewById(R.id.rvJadwal);
        btnPesan = findViewById(R.id.btnPesanSekarang);
        tvJamTerpilih = findViewById(R.id.tvJamTerpilih);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);
        tvNamaLapangan.setText("Jadwal " + namaLapanganDiterima);
        tvTanggal.setText(getTanggalHariIni());
        listJadwal = new ArrayList<>();
        adapter = new JadwalAdapter(this, listJadwal, this);
        rvJadwal.setLayoutManager(new LinearLayoutManager(this));
        rvJadwal.setAdapter(adapter);
        btnKembali.setOnClickListener(v -> finish());
        btnPesan.setOnClickListener(v -> {
            Intent intent = new Intent(BookingActivity.this, FormActivity.class);
            ArrayList<String> jamBookingList = new ArrayList<>();
            for (JadwalModel model : slotTerpilih) {
                jamBookingList.add(model.getJam());
            }
            intent.putStringArrayListExtra("JAM_BOOKING_LIST", jamBookingList);
            intent.putExtra("NAMA_LAPANGAN", namaLapanganDiterima);
            intent.putExtra("TOTAL_HARGA", hargaPerJam * slotTerpilih.size());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJadwalFromSQLite();
    }

    private void loadJadwalFromSQLite() {
        try {
            adapter.clear();

            ArrayList<JadwalModel> schedules = dbHelper.getAllSchedules();

            for (JadwalModel model : schedules) {
                adapter.add(model);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error saat memuat data SQLite: " + e.getMessage());
            Toast.makeText(this, "Gagal memuat jadwal dari database", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTanggalHariIni() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        return sdf.format(new Date());
    }

    @Override
    public void onSelectionChanged(ArrayList<JadwalModel> selectedSlots) {
        this.slotTerpilih = selectedSlots;
        int jumlahJam = selectedSlots.size();

        if (jumlahJam == 0) {
            tvJamTerpilih.setText("Jam Terpilih: (Tidak ada)");
            tvTotalHarga.setText("Total Harga: Rp 0");
            btnPesan.setText("Pilih Jam");
            btnPesan.setEnabled(false);
        } else {
            int total = hargaPerJam * jumlahJam;
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            tvTotalHarga.setText("Total Harga: " + formatter.format(total));

            StringBuilder jamBuilder = new StringBuilder();
            jamBuilder.append("Jam Terpilih: ");
            for (int i = 0; i < selectedSlots.size(); i++) {
                jamBuilder.append(selectedSlots.get(i).getJam().substring(0, 5));
                if (i < selectedSlots.size() - 1) {
                    jamBuilder.append(", ");
                }
            }
            tvJamTerpilih.setText(jamBuilder.toString());
            btnPesan.setText("Pesan Sekarang (" + jumlahJam + " Jam)");
            btnPesan.setEnabled(true);
        }
    }
}