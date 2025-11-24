package com.example.nathanaelfutsal;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LapanganAdapter adapter;
    ArrayList<LapanganModel> listLapangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listLapangan = new ArrayList<>();
        isiDataDummy();

        recyclerView = findViewById(R.id.recyclerViewLapang);

        adapter = new LapanganAdapter(this, listLapangan);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void isiDataDummy() {

        listLapangan.add(new LapanganModel(
                "Lapangan A (Sintetis)",
                "Rp 150.000 / jam",
                "Tersedia",
                R.color.purple_200
        ));

        listLapangan.add(new LapanganModel(
                "Lapangan B (Semen)",
                "Rp 120.000 / jam",
                "Penuh",
                R.color.purple_500
        ));

        listLapangan.add(new LapanganModel(
                "Lapangan C (VIP)",
                "Rp 200.000 / jam",
                "Tersedia",
                R.color.purple_700
        ));
    }
}