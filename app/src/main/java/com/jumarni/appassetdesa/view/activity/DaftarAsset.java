package com.jumarni.appassetdesa.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataAssetDesaModel;
import com.jumarni.appassetdesa.presentasi.DaftarAssetAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DaftarAsset extends AppCompatActivity {

    private ImageView btn_kembali;
    private RecyclerView rc_data;
    private SwipeRefreshLayout sw_data;
    private RecyclerView.LayoutManager layoutManager;
    private StringRequest getAsetDesa;
    public static ArrayList<DataAssetDesaModel> dataAssetDesaModels;
    private SharedPreferences preferences;
    private DaftarAssetAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_asset);
        init();
    }

    private void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        rc_data = findViewById(R.id.rc_data);
        sw_data = findViewById(R.id.sw_data);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        sw_data.setOnRefreshListener(this::setGetAsetDesa);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetAsetDesa();
    }

    public void setGetAsetDesa() {
        dataAssetDesaModels = new ArrayList<>();
        sw_data.setRefreshing(true);

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
                        dataAssetDesaModels.add(getAsset);

                    }
                    adapter = new DaftarAssetAdapter(this, dataAssetDesaModels);
                    rc_data.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    rc_data.setAdapter(adapter);
                } else {
                    Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sw_data.setRefreshing(false);
        }, error -> {
            sw_data.setRefreshing(false);
            error.printStackTrace();
        });
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getAsetDesa);
    }
}