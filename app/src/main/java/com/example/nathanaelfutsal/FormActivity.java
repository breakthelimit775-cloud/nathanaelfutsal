package com.example.nathanaelfutsal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FormActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    TextView tvDetailBooking;
    EditText etNama, etWa, etAlamat;
    Button btnPilihGambar, btnSimpan;
    ImageView imgPreview;
    DBHelper dbHelper;
    String namaLapangan;
    ArrayList<String> jamBookingList;
    int totalHarga;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        dbHelper = new DBHelper(this);
        tvDetailBooking = findViewById(R.id.tvDetailBooking);
        etNama = findViewById(R.id.etNamaForm);
        etWa = findViewById(R.id.etWaForm);
        etAlamat = findViewById(R.id.etAlamatForm);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgPreview = findViewById(R.id.imgPreview);
        namaLapangan = getIntent().getStringExtra("NAMA_LAPANGAN");
        jamBookingList = getIntent().getStringArrayListExtra("JAM_BOOKING_LIST");
        totalHarga = getIntent().getIntExtra("TOTAL_HARGA", 0);

        String jamTerformat = String.join(", ", jamBookingList);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String hargaTerformat = formatter.format(totalHarga);

        tvDetailBooking.setText("Booking " + namaLapangan +
                "\nJam: " + jamTerformat +
                "\nTotal: " + hargaTerformat);

        btnPilihGambar.setOnClickListener(v -> bukaGaleri());
        btnSimpan.setOnClickListener(v -> simpanData());
    }

    private void bukaGaleri() {
        Intent intent = new Intent();
        intent.setType("image/jpeg,image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Bukti Transfer"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri);
            imgPreview.setVisibility(View.VISIBLE);
        }
    }

    private void simpanData() {
        String nama = etNama.getText().toString().trim();
        String wa = etWa.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();

        if (nama.isEmpty() || wa.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data diri", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Harap upload bukti pembayaran", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            for (String jam : jamBookingList) {
                dbHelper.updateScheduleStatus(jam, "dipesan");
            }
            Toast.makeText(this, "Booking berhasil disimpan!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("FormActivity", "Error saat update database: " + e.getMessage());
            Toast.makeText(this, "Gagal menyimpan booking.", Toast.LENGTH_SHORT).show();
        }
    }
}