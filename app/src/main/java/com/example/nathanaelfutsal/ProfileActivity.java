package com.example.nathanaelfutsal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    EditText etName, etPhone;
    TextView etEmail;
    Button btnSave, btnLogout;

    ImageView ivProfilePicture, ivEditPhoto;
    TextView tvProfileNameDisplay, tvProfileEmailDisplay;
    CardView cvHistory;

    private String currentPhotoUri;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    setImageViewFromUri(uri);
                    currentPhotoUri = uri.toString();
                    savePhotoUri(currentPhotoUri);
                    Toast.makeText(this, "Foto berhasil diubah!", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etProfileName);
        etEmail = findViewById(R.id.etProfileEmail);
        etPhone = findViewById(R.id.etProfilePhone);
        btnSave = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogoutProfile);

        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        ivEditPhoto = findViewById(R.id.ivEditPhoto);
        tvProfileNameDisplay = findViewById(R.id.tvProfileNameDisplay);
        tvProfileEmailDisplay = findViewById(R.id.tvProfileEmailDisplay);
        cvHistory = findViewById(R.id.cvHistory);

        loadData();

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> saveData());
        }
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> tampilkanDialogLogout());
        }
        if (ivEditPhoto != null) {
            ivEditPhoto.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        }

        if (cvHistory != null) {
            cvHistory.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                startActivity(intent);
            });
        }

        if (etEmail != null) {
            etEmail.setEnabled(false);
        }
    }

    private void loadData() {
        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);

        String name = pref.getString("name", "Pengguna");
        String email = pref.getString("email", "N/A");
        String phone = pref.getString("phone", "");
        currentPhotoUri = pref.getString("photo_uri", null);

        if (etName != null) etName.setText(name);
        if (etEmail != null) etEmail.setText(email);
        if (etPhone != null) etPhone.setText(phone);

        if (tvProfileNameDisplay != null) tvProfileNameDisplay.setText(name);
        if (tvProfileEmailDisplay != null) tvProfileEmailDisplay.setText(email);
        if (currentPhotoUri != null && !currentPhotoUri.isEmpty()) {
            setImageViewFromUri(Uri.parse(currentPhotoUri));
        } else {
            if (ivProfilePicture != null) {
                ivProfilePicture.setImageResource(R.drawable.ic_default_profile);
            }
        }
    }

    private void setImageViewFromUri(Uri imageUri) {
        if (ivProfilePicture == null) return;

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            ivProfilePicture.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                ivProfilePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException ex) {
                ivProfilePicture.setImageResource(R.drawable.ic_default_profile);
                Toast.makeText(this, "Gagal memuat foto.", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            ivProfilePicture.setImageResource(R.drawable.ic_default_profile);
        }
    }

    private void savePhotoUri(String uri) {
        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        pref.edit().putString("photo_uri", uri).apply();
    }

    private void saveData() {
        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (etName != null) editor.putString("name", etName.getText().toString());
        if (etPhone != null) editor.putString("phone", etPhone.getText().toString());

        editor.apply();

        if (tvProfileNameDisplay != null) tvProfileNameDisplay.setText(etName.getText().toString());

        Toast.makeText(this, "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show();
    }

    private void tampilkanDialogLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> prosesLogoutLokal())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void prosesLogoutLokal() {
        SharedPreferences pref = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        pref.edit().clear().apply();

        Toast.makeText(ProfileActivity.this, "Berhasil Logout. Harap Login Ulang.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}