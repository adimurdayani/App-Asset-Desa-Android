package com.jumarni.appassetdesa.view.activity;

import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.Dusun;
import com.jumarni.appassetdesa.model.Riwayat;
import com.jumarni.appassetdesa.presentasi.PendudukAdapter;
import com.jumarni.appassetdesa.presentasi.RiwayatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RiwayatActivity extends AppCompatActivity {

    private ImageView btn_kembali, btn_setting;
    private StringRequest getriwayat;
    public ArrayList<Riwayat> riwayatArrayList;
    private RiwayatAdapter adapter;
    private SwipeRefreshLayout sw_data;
    public RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private SearchView searchView;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        init();
        setButton();
    }

    public void setGetriwayat() {
        riwayatArrayList = new ArrayList<>();
        sw_data.setRefreshing(true);
        getriwayat = new StringRequest(Request.Method.GET, URLServer.GETRIWAYAT + id, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        Riwayat getPenduduk = new Riwayat();
                        getPenduduk.setNama_aset(getData.getString("nama_aset"));
                        getPenduduk.setCreated_at(getData.getString("created_at"));
                        getPenduduk.setTgl_kembali(getData.getString("tgl_kembali"));
                        getPenduduk.setHarga(getData.getString("harga"));
                        getPenduduk.setStatus(getData.getInt("status"));
                        riwayatArrayList.add(getPenduduk);
                    }
                    adapter = new RiwayatAdapter(this, riwayatArrayList);
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String generate_token = preferences.getString("token_id", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("token_generate", generate_token);
                return map;
            }
        };
        setKoneksiError();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getriwayat);
    }

    private void setKoneksiError() {
        getriwayat.setRetryPolicy(new RetryPolicy() {
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
        sw_data.setOnRefreshListener(this::setGetriwayat);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getSearchData().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setGetriwayat();
                return false;
            }
        });
    }

    private void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_setting = findViewById(R.id.setting);
        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        searchView = findViewById(R.id.search_data);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

        id = preferences.getInt("id_regis", 0);
    }

    @Override
    protected void onResume() {
        setGetriwayat();
        super.onResume();
    }
}