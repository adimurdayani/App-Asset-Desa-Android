package com.jumarni.appassetdesa.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
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

public class HomeActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private TextView txt_nama;
    private SwipeRefreshLayout sw_data;
    public static RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private CardView btn_asset, btn_kritik,  btn_penduduk;
    private StringRequest getKritiklimit;
    public static ArrayList<DataKritikModel> dataKritikModels;
    private KritikLimitAdapter adapter;
    private ImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        txt_nama = findViewById(R.id.nama);
        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        btn_asset = findViewById(R.id.btn_sewa);
        btn_kritik = findViewById(R.id.btn_kritik);
        btn_penduduk = findViewById(R.id.btn_penduduk);
        setting = findViewById(R.id.setting);

        txt_nama.setText(preferences.getString("nama", ""));

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

        btn_penduduk.setOnClickListener(v -> {
            startActivity(new Intent(this, PendudukActivity.class));
        });

        setting.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });

        btn_asset.setOnClickListener(v -> {
            startActivity(new Intent(this, SewaActivity.class));
        });

        btn_kritik.setOnClickListener(v -> {
            startActivity(new Intent(this, KritikActivity.class));
        });

        sw_data.setOnRefreshListener(this::setGetKritiklimit);
    }

    public void setGetKritiklimit() {
        dataKritikModels = new ArrayList<>();
        sw_data.setRefreshing(true);

        getKritiklimit = new StringRequest(Request.Method.GET, URLServer.GETKRITIKLIMIT, response -> {
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
        koneksi.add(getKritiklimit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetKritiklimit();
    }
}