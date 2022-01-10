package com.jumarni.appassetdesa.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.helper.FormatRupiah;
import com.jumarni.appassetdesa.model.AsetTidakDisewakan;
import com.jumarni.appassetdesa.model.DataKritikModel;
import com.jumarni.appassetdesa.presentasi.DaftarAssetTdkDisewakanAdapter;
import com.jumarni.appassetdesa.presentasi.KritikLimitAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

@SuppressLint("SetTextI18n")
public class SewaActivity extends AppCompatActivity {
    private LinearLayout btn_pilih, div_datasewa;
    private ImageView btn_kembali, btn_setting;
    private TextView txt_namaaset, txt_hargaaset;
    private TextInputEditText e_harga, e_namaaset, e_jumlahbarang;
    private Button btn_kirim;
    private String harga, nama_aset, id, asset_id, jml_aset;
    private ProgressDialog dialog;
    private TextInputLayout l_harga, l_namaaset, l_jumlahbarang;
    private StringRequest kirimData;
    private SharedPreferences preferences;
    private RecyclerView rc_data;
    private TextView text_aset_id;
    private DaftarAssetTdkDisewakanAdapter adapter;
    private int jml_barang, jml_item;
    private ArrayList<AsetTidakDisewakan> asetTidakDisewakans;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewa);
        init();
    }

    private void getAset() {
        asetTidakDisewakans = new ArrayList<>();
        kirimData = new StringRequest(Request.Method.GET, URLServer.GETASETTIDAKDISEWAKAN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        AsetTidakDisewakan getaset = new AsetTidakDisewakan();
                        getaset.setNama_barang(getData.getString("nama_barang"));
                        getaset.setJml_barang(getData.getString("jml_barang"));
                        asetTidakDisewakans.add(getaset);
                    }
                    adapter = new DaftarAssetTdkDisewakanAdapter(this, asetTidakDisewakans);
                    rc_data.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    rc_data.setAdapter(adapter);
                } else {
                    showError(object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showError(e.toString());
            }
        }, error -> {
            Log.d("respon", "err: " + error.networkResponse);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_aset", asset_id);
                map.put("jml_aset", jml_aset);
                return map;
            }
        };
        setKoneksiInternet();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(kirimData);
    }

    @SuppressLint("SetTextI18n")
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
        div_datasewa = findViewById(R.id.div_datasewa);
        e_jumlahbarang = findViewById(R.id.e_jumlahbarang);
        l_jumlahbarang = findViewById(R.id.l_jumlahbarang);
        rc_data = findViewById(R.id.rc_data);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        e_namaaset.setText(getIntent().getStringExtra("nama_aset"));
        e_harga.setText(getIntent().getStringExtra("harga_aset"));
        text_aset_id.setText("" + getIntent().getIntExtra("id_aset", 0));
        jml_item = getIntent().getIntExtra("jml_aset", 0);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);

        setButton();
        cekvalidasi();
        getJumlahbarang();
    }

    private void setButton() {
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
                if (jml_item <= 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setIcon(R.drawable.ic_baseline_info_24);
                    dialog.setTitle("Item Tidak Ada!");
                    dialog.setMessage("Aset yang dipilih belum tersedia. Silahkan pilih aset yang lain!");
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    setKirimData();
                    setUpdatebarang();
                }
            }
        });
    }

    private void getJumlahbarang() {
        jml_barang = getIntent().getIntExtra("id_aset", 0);
        if (jml_barang != 0) {
            l_jumlahbarang.setVisibility(View.VISIBLE);
            div_datasewa.setVisibility(View.VISIBLE);

            FormatRupiah formatRupiah = new FormatRupiah();
            String harga_aset = getIntent().getStringExtra("harga_aset");
            String hasil = formatRupiah.formatRupiah(Double.parseDouble(harga_aset));

            txt_hargaaset.setText(hasil);
            txt_namaaset.setText(getIntent().getStringExtra("nama_aset"));
        } else {
            l_jumlahbarang.setVisibility(View.GONE);
            div_datasewa.setVisibility(View.GONE);
        }
    }

    public void setKirimData() {
        dialog.setMessage("Loading...");
        dialog.show();
        kirimData = new StringRequest(Request.Method.POST, URLServer.POSTPENYEWA, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    showDialog();
                } else {
                    showError(object.getString("message"));
                }
            } catch (JSONException e) {
                showError(e.toString());
            }
            dialog.dismiss();
        }, error -> {
            dialog.dismiss();
            Log.d("respon", "err: " + error.networkResponse);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", id);
                map.put("aset_id", asset_id);
                map.put("harga_id", asset_id);
                return map;
            }
        };
        setKoneksiInternet();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(kirimData);
    }

    public void setUpdatebarang() {
        kirimData = new StringRequest(Request.Method.POST, URLServer.POSTASSET, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {

                } else {
                    showError(object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showError(e.toString());
            }
        }, error -> {
            Log.d("respon", "err: " + error.networkResponse);
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_aset", asset_id);
                map.put("jml_aset", jml_aset);
                return map;
            }
        };
        setKoneksiInternet();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(kirimData);
    }

    private void showDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses!")
                .show();
    }

    private void setKoneksiInternet() {
        kirimData.setRetryPolicy(new RetryPolicy() {
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

    public void getinputtext() {
        harga = e_harga.getText().toString().trim();
        nama_aset = e_namaaset.getText().toString().trim();
        id = String.valueOf(preferences.getInt("id_regis", 0));
        asset_id = text_aset_id.getText().toString().trim();
        jml_aset = e_jumlahbarang.getText().toString().trim();

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
        e_jumlahbarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (jml_aset.isEmpty()) {
                    l_jumlahbarang.setErrorEnabled(false);
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

        if (jml_aset.isEmpty()) {
            l_jumlahbarang.setErrorEnabled(true);
            l_jumlahbarang.setError("Kolom jumlah barang tidak boleh kosong!");
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        getAset();
        super.onResume();
    }
}