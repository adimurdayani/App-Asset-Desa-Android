package com.jumarni.appassetdesa.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataPendudukModel;
import com.jumarni.appassetdesa.model.Namakk;
import com.jumarni.appassetdesa.presentasi.DetailARTAdapter;
import com.jumarni.appassetdesa.presentasi.DetailPendudukAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailKkActivity extends AppCompatActivity {
    private ImageView btn_kembali, btn_setting;
    private StringRequest getPenduduk;
    private ArrayList<DataPendudukModel> pendudukModels;
    private DetailARTAdapter adapter;
    private SwipeRefreshLayout sw_data;
    private RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private SearchView searchView;
    private int id_kk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kk);
        id_kk = getIntent().getIntExtra("kk_id", 0);
        setinit();
        setDisplay();
        setButton();
    }
    public void setGetPenduduk() {
        pendudukModels = new ArrayList<>();
        sw_data.setRefreshing(true);
        getPenduduk = new StringRequest(Request.Method.GET, URLServer.GETPENDUDUK + id_kk, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        DataPendudukModel p = new DataPendudukModel();
                        p.setNo_kk(getData.getString("no_kk"));
                        p.setNik(getData.getString("nik"));
                        p.setTgl_lahir(getData.getString("tgl_lahir"));
                        p.setNama_art(getData.getString("nama_art"));
                        p.setKelamin(getData.getString("kelamin"));
                        pendudukModels.add(p);
                    }
                    adapter = new DetailARTAdapter(this, pendudukModels);
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
        koneksi.add(getPenduduk);
    }

    private void setKoneksiInternet() {
        getPenduduk.setRetryPolicy(new RetryPolicy() {
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

    private void setButton() {
        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btn_setting.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });
        sw_data.setOnRefreshListener(this::setGetPenduduk);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getSearchData().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setGetPenduduk();
                return false;
            }
        });
    }

    private void setDisplay() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);
    }

    private void setinit() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_setting = findViewById(R.id.setting);
        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        searchView = findViewById(R.id.search_data);
    }

    @Override
    protected void onResume() {
        setGetPenduduk();
        super.onResume();
    }
}