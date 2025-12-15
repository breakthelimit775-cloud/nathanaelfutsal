package com.example.nathanaelfutsal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {

    private TextView tvTotalHargaForm, tvJamTerpilihForm;
    private EditText etNamaPenyewa, etNomorTelepon;
    private ImageView ivBuktiTransfer;
    private Button btnUploadBukti, btnSelesaikan;

    private DBHelper dbHelper;
    private String namaLapangan;
    private int totalHarga;
    private ArrayList<String> jamBookingList;
    private String imageString = "";
    private String userEmail = "";

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    ivBuktiTransfer.setImageURI(uri);
                    imageString = uri.toString();
                    btnUploadBukti.setText("Ubah Bukti Transfer");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        dbHelper = new DBHelper(this);

        tvTotalHargaForm = findViewById(R.id.tvTotalHargaForm);
        tvJamTerpilihForm = findViewById(R.id.tvJamTerpilihForm);
        etNamaPenyewa = findViewById(R.id.etNamaPenyewa);
        etNomorTelepon = findViewById(R.id.etNomorTelepon);
        ivBuktiTransfer = findViewById(R.id.ivBuktiTransfer);
        btnUploadBukti = findViewById(R.id.btnUploadBukti);
        btnSelesaikan = findViewById(R.id.btnSelesaikanPemesanan);

        namaLapangan = getIntent().getStringExtra("NAMA_LAPANGAN");
        totalHarga = getIntent().getIntExtra("TOTAL_HARGA", 0);
        jamBookingList = getIntent().getStringArrayListExtra("JAM_BOOKING_LIST");

        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        userEmail = pref.getString("email", "");
        etNamaPenyewa.setText(pref.getString("name", ""));
        etNomorTelepon.setText(pref.getString("phone", ""));

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tvTotalHargaForm.setText("Total Harga: " + formatter.format(totalHarga));

        StringBuilder jamBuilder = new StringBuilder();
        if (jamBookingList != null && !jamBookingList.isEmpty()) {
            String jamMulai = jamBookingList.get(0).substring(0, 5);
            String jamSelesai = jamBookingList.get(jamBookingList.size() - 1).substring(6, 11);
            jamBuilder.append("Waktu Booking: ").append(jamMulai).append(" - ").append(jamSelesai);
        } else {
            jamBuilder.append("Waktu Booking: N/A");
        }
        tvJamTerpilihForm.setText(jamBuilder.toString());

        btnUploadBukti.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSelesaikan.setOnClickListener(v -> {
            if (imageString.isEmpty()) {
                Toast.makeText(FormActivity.this, "Mohon unggah bukti transfer.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (jamBookingList != null) {
                for (String jam : jamBookingList) {
                    boolean success = dbHelper.updateScheduleStatus(jam, "dipesan", imageString, userEmail);
                    if (!success) {
                        Toast.makeText(FormActivity.this, "Gagal memperbarui status jam: " + jam, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(FormActivity.this, "Pemesanan berhasil! Menunggu konfirmasi admin.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}