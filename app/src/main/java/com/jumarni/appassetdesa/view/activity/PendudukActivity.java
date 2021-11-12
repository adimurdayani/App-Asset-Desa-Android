package com.jumarni.appassetdesa.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.jumarni.appassetdesa.model.DataPendudukModel;
import com.jumarni.appassetdesa.presentasi.PendudukAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PendudukActivity extends AppCompatActivity {
    private ImageView btn_kembali, btn_setting;
    private StringRequest getPenduduk, getJmlPenduduk, getJmlRT;
    public static ArrayList<DataPendudukModel> pendudukModels;
    private PendudukAdapter adapter;
    private SwipeRefreshLayout sw_data;
    public static RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences preferences;
    private SearchView searchView;
    private TextView jml_rt, jml_penduduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penduduk);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_setting = findViewById(R.id.setting);
        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        searchView = findViewById(R.id.search_data);
        jml_penduduk = findViewById(R.id.jml_penduduk);
        jml_rt = findViewById(R.id.jml_rt);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

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
                setGetJmlPenduduk();
                return false;
            }
        });
    }

    public void setGetPenduduk() {
        pendudukModels = new ArrayList<>();
        sw_data.setRefreshing(true);

        getPenduduk = new StringRequest(Request.Method.GET, URLServer.GETPENDUDUK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject getData = data.getJSONObject(i);

                        DataPendudukModel getPenduduk = new DataPendudukModel();
                        getPenduduk.setNama_kk(getData.getString("nama_kk"));
                        getPenduduk.setRt(getData.getString("rt"));
                        getPenduduk.setNama_dusun(getData.getString("nama_dusun"));
                        getPenduduk.setTgl_lahir(getData.getString("tgl_lahir"));
                        getPenduduk.setCreated_at(getData.getString("created_at"));
                        pendudukModels.add(getPenduduk);

                    }
                    adapter = new PendudukAdapter(this, pendudukModels);
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
        koneksi.add(getPenduduk);
    }

    public void setGetJmlPenduduk() {
        sw_data.setRefreshing(true);
        getJmlPenduduk = new StringRequest(Request.Method.GET, URLServer.GETJUMLAHPENDUDUK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    jml_penduduk.setText("" + object.getInt("data"));
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getJmlPenduduk);
    }

    public void setGetJmlRT() {
        sw_data.setRefreshing(true);
        getJmlPenduduk = new StringRequest(Request.Method.GET, URLServer.GETJUMLAHRT, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    jml_rt.setText("" + object.getInt("data"));
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
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getJmlPenduduk);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGetPenduduk();
        setGetJmlRT();
        setGetJmlPenduduk();
    }
}