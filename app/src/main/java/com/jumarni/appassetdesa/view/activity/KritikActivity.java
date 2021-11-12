package com.jumarni.appassetdesa.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.jumarni.appassetdesa.model.DataKritikModel;
import com.jumarni.appassetdesa.presentasi.KritikLimitAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class KritikActivity extends AppCompatActivity {
    private ImageView btn_kembali, btn_setting;
    private CardView btn_add;
    private StringRequest getKritik;
    public static ArrayList<DataKritikModel> dataKritikModels;
    private KritikLimitAdapter adapter;
    private SwipeRefreshLayout sw_data;
    public static RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kritik);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_setting = findViewById(R.id.setting);
        btn_add = findViewById(R.id.btn_add);

        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        searchView = findViewById(R.id.search_data);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btn_add.setOnClickListener(v -> {
            startActivity(new Intent(this, TambahKritikActivity.class));
        });

        btn_setting.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });

        sw_data.setOnRefreshListener(this::setGetKritik);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getSearchData().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void setGetKritik() {
        dataKritikModels = new ArrayList<>();
        sw_data.setRefreshing(true);
        getKritik = new StringRequest(Request.Method.GET, URLServer.GETKRITIK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject getData = data.getJSONObject(i);

                        DataKritikModel getDataPengaduan = new DataKritikModel();
                        getDataPengaduan.setId_kritik(getData.getInt("id_kritik"));
                        getDataPengaduan.setNama(getData.getString("nama"));
                        getDataPengaduan.setAdmin_id(getData.getInt("admin_id"));
                        getDataPengaduan.setKritik(getData.getString("kritik"));
                        getDataPengaduan.setCreated_at(getData.getString("created_at"));
                        dataKritikModels.add(getDataPengaduan);

                    }
                    adapter = new KritikLimitAdapter(this, dataKritikModels);
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
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getKritik);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetKritik();
    }
}