package com.example.nathanaelfutsal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    ImageView btnKembali, btnCall, imgDetailFoto;
    TextView tvDetailNama, tvDetailHarga, tvDetailSpek;
    Button btnBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnKembali = findViewById(R.id.btnKembali);
        btnCall = findViewById(R.id.btnCall);
        imgDetailFoto = findViewById(R.id.imgDetailFoto);
        tvDetailNama = findViewById(R.id.tvDetailNama);
        tvDetailHarga = findViewById(R.id.tvDetailHarga);
        tvDetailSpek = findViewById(R.id.tvDetailSpek);
        btnBooking = findViewById(R.id.btnBooking);

        Intent intent = getIntent();
        String nama = intent.getStringExtra("NAMA_LAPANGAN");
        String harga = intent.getStringExtra("HARGA_LAPANGAN");
        int foto = intent.getIntExtra("FOTO_LAPANGAN", R.color.purple_500);

        tvDetailNama.setText(nama);
        tvDetailHarga.setText(harga);
        imgDetailFoto.setImageResource(foto);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, BookingActivity.class);
                intent.putExtra("NAMA_LAPANGAN", tvDetailNama.getText().toString());
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomorTelepon = "081234567890";
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + nomorTelepon));
                startActivity(dialIntent);
            }
        });
    }
}