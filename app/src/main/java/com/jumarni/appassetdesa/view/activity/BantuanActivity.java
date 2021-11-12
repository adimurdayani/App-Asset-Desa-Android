package com.jumarni.appassetdesa.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jumarni.appassetdesa.R;


public class BantuanActivity extends AppCompatActivity {
    private ImageView btn_kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        init();
    }

    public void init() {

        btn_kembali = findViewById(R.id.btn_kembali);
        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }
}