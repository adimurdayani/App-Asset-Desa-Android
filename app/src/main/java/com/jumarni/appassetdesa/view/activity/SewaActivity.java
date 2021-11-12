package com.jumarni.appassetdesa.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SewaActivity extends AppCompatActivity {
    private LinearLayout btn_pilih;
    private ImageView btn_kembali, btn_setting;
    private TextView txt_namaaset, txt_hargaaset;
    private TextInputEditText e_harga, e_namaaset;
    private Button btn_kirim;
    private String harga, nama_aset, id,asset_id, harga_id;
    private ProgressDialog dialog;
    private TextInputLayout l_harga, l_namaaset;
    private StringRequest kirimData;
    private SharedPreferences preferences;
    private TextView text_aset_id,txt_hargaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewa);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_pilih = findViewById(R.id.btn_pilih);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_setting = findViewById(R.id.setting);
        txt_hargaaset = findViewById(R.id.harga);
        txt_namaaset = findViewById(R.id.nama_aset);
        btn_kirim = findViewById(R.id.btn_kirim);
        l_harga = findViewById(R.id.l_harga);
        l_namaaset = findViewById(R.id.l_nama_aset);
        e_harga = findViewById(R.id.e_harga);
        e_namaaset = findViewById(R.id.e_nama_aset);
        text_aset_id = findViewById(R.id.asset_id);
        txt_hargaid = findViewById(R.id.harga_id);

        txt_hargaaset.setText(getIntent().getStringExtra("harga_aset"));
        txt_namaaset.setText(getIntent().getStringExtra("nama_aset"));
        e_namaaset.setText(getIntent().getStringExtra("nama_aset"));
        e_harga.setText(getIntent().getStringExtra("harga_aset"));
        text_aset_id.setText(getIntent().getStringExtra("id_aset"));
        txt_hargaid.setText(getIntent().getStringExtra("id_aset"));

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btn_setting.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });

        btn_pilih.setOnClickListener(v -> {
            startActivity(new Intent(this, DaftarAsset.class));
            finish();
        });
        btn_kirim.setOnClickListener(v -> {
            if (validasi()) {
                setKirimData();
            }
        });
        cekvalidasi();
    }

    public void setKirimData() {
        dialog.setMessage("Loading...");
        dialog.show();
        kirimData = new StringRequest(Request.Method.POST, URLServer.POSTPENYEWA, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    Toast.makeText(this, "Data telah terikim!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }, error -> {
            dialog.dismiss();
            error.printStackTrace();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", id);
                map.put("aset_id", asset_id);
                map.put("harga_id", harga_id);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(kirimData);
    }

    public void getinputtext() {
        harga = e_harga.getText().toString().trim();
        nama_aset = e_namaaset.getText().toString().trim();
        id = String.valueOf(preferences.getInt("id_regis", 0));
        asset_id = text_aset_id.getText().toString().trim();
        harga_id = txt_hargaid.getText().toString().trim();
    }

    public void cekvalidasi() {
        getinputtext();
        e_namaaset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nama_aset.isEmpty()) {
                    l_namaaset.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_harga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (harga.isEmpty()) {
                    l_harga.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validasi() {
        getinputtext();
        if (nama_aset.isEmpty()) {
            l_namaaset.setErrorEnabled(true);
            l_namaaset.setError("Kolom nama aset tidak boleh kosong!");
            return false;
        }
        if (harga.isEmpty()) {
            l_harga.setErrorEnabled(true);
            l_harga.setError("Kolom harga tidak boleh kosong!");
            return false;
        }

        return true;
    }
}