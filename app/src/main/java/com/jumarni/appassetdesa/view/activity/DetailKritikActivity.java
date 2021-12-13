package com.jumarni.appassetdesa.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataKritikModel;
import com.jumarni.appassetdesa.presentasi.KritikLimitAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailKritikActivity extends AppCompatActivity {
    private ImageView btn_kembali;
    private TextView txt_namauser, txt_namaadmin, txt_tgluser, txt_tgladmin, txt_kritik, txt_jawaban, txt_idkritik;
    private SharedPreferences preferences;
    private StringRequest getDetailkritik;
    private String id_kritik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kritik);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        txt_jawaban = findViewById(R.id.jawaban);
        txt_kritik = findViewById(R.id.kritik);
        txt_namaadmin = findViewById(R.id.nama_admin);
        txt_namauser = findViewById(R.id.nama);
        txt_tgladmin = findViewById(R.id.tgl_kirim);
        txt_tgluser = findViewById(R.id.tanggal);
        txt_idkritik = findViewById(R.id.id_kritik);

        txt_idkritik.setText(getIntent().getStringExtra("id_kritik"));
        id_kritik = txt_idkritik.getText().toString().trim();

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }

    public void setGetDetailkritik() {
        getDetailkritik = new StringRequest(Request.Method.GET, URLServer.GETKRITIKID + id_kritik, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    txt_namauser.setText(data.getString("nama"));
                    txt_namaadmin.setText(data.getString("nama_grup"));
                    txt_tgluser.setText(data.getString("created_at"));
                    txt_tgladmin.setText(data.getString("tgl_kirim"));
                    txt_kritik.setText(data.getString("kritik"));
                    txt_jawaban.setText(data.getString("jawaban"));
                } else {
                    showError(object.getString("message"));
                }
            } catch (JSONException e) {
                showError(e.toString());
            }
        }, error -> {
            Log.d("respon", "err: " + error.networkResponse);
        });
        setKoneksiInternet();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getDetailkritik);
    }


    private void setKoneksiInternet() {
        getDetailkritik.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 2000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                    showError("Koneksi gagal");
                }
            }
        });
    }

    private void showError(String string) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(string)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetDetailkritik();
    }
}