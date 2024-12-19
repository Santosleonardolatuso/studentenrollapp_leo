package com.millenialzdev.myloginandregistersqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnKeluar;

    public static final String SHARED_PREF_NAME = "myPref";
    public static final String LOGIN_STATUS = "isLoggedIn";

    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnKeluar = findViewById(R.id.btnKeluar);

        // Inisialisasi Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Cek status login
        boolean isLoggedIn = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        if (!isLoggedIn) {
            // Jika tidak login, kembali ke halaman login
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
            finish();
        }

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout: Hapus status login di Shared Preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LOGIN_STATUS, false);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Berhasil Keluar", Toast.LENGTH_LONG).show();

                // Kembali ke halaman login
                Intent keluar = new Intent(getApplicationContext(), Login.class);
                startActivity(keluar);
                finish();
            }
        });
    }
}
