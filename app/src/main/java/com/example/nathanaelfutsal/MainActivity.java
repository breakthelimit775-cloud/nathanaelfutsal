package com.example.nathanaelfutsal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LapanganAdapter adapter;
    ArrayList<LapanganModel> listLapangan;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        listLapangan = new ArrayList<>();
        isiDataDummy();

        recyclerView = findViewById(R.id.recyclerViewLapang);
        adapter = new LapanganAdapter(this, listLapangan);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            prosesLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prosesLogout() {
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {

            Toast.makeText(MainActivity.this, "Berhasil Log Out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
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
                "Tersedia",
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