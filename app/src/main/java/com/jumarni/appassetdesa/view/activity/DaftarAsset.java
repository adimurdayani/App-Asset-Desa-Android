package com.jumarni.appassetdesa.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.Barang;
import com.jumarni.appassetdesa.model.DataAssetDesaModel;
import com.jumarni.appassetdesa.presentasi.DaftarAssetAdapter;
import com.jumarni.appassetdesa.room.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DaftarAsset extends AppCompatActivity {

    private ImageView btn_kembali;
    private RecyclerView rc_data;
    private SwipeRefreshLayout sw_data;
    private RecyclerView.LayoutManager layoutManager;
    private StringRequest getAsetDesa;
    public static ArrayList<DataAssetDesaModel> dataAssetDesaModels;
    private SharedPreferences preferences;
    private DaftarAssetAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_asset);
        init();
        setButton();
    }

    private void setButton() {
        btn_kembali.setOnClickListener(v -> {
            startActivity(new Intent(this, SewaActivity.class));
            finish();
        });

        sw_data.setOnRefreshListener(this::setGetAsetDesa);
    }

    private void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        rc_data = findViewById(R.id.rc_data);
        sw_data = findViewById(R.id.sw_data);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetAsetDesa();
    }

    public void setGetAsetDesa() {
        dataAssetDesaModels = new ArrayList<>();
        sw_data.setRefreshing(false);
        getAsetDesa = new StringRequest(Request.Method.GET, URLServer.GETASSET, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        DataAssetDesaModel getAsset = new DataAssetDesaModel();
                        getAsset.setId_aset(getData.getInt("id_aset"));
                        getAsset.setNama_aset(getData.getString("nama_aset"));
                        getAsset.setHarga(getData.getString("harga"));
                        getAsset.setJml_aset(getData.getInt("jml_aset"));
                        dataAssetDesaModels.add(getAsset);
                    }
                    adapter = new DaftarAssetAdapter(this, dataAssetDesaModels);
                    rc_data.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    rc_data.setAdapter(adapter);
                } else {
                    showError(object.getString("message"));
                }
            } catch (JSONException e) {
                showError(e.toString());
            }
            sw_data.setRefreshing(false);
        }, error -> {
            sw_data.setRefreshing(false);
            Log.d("respon", "err: " + error.networkResponse);
        });
        setKoneksiInternet();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getAsetDesa);
    }

    private void setKoneksiInternet() {
        getAsetDesa.setRetryPolicy(new RetryPolicy() {
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
}