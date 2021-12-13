package com.jumarni.appassetdesa.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jumarni.appassetdesa.R;
import com.jumarni.appassetdesa.api.URLServer;
import com.jumarni.appassetdesa.model.DataKritikModel;
import com.jumarni.appassetdesa.model.DataUserRegis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TambahKritikActivity extends AppCompatActivity {
    private ImageView btn_kembali;
    private Button btn_kirim;
    private TextInputEditText e_kritik;
    private TextInputLayout l_kritik;
    public String user_id, kritik;
    private TextView txt_userid;
    private SharedPreferences preferences;
    private StringRequest kirimData;
    private ArrayList<DataKritikModel> dataKritikModels;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kritik);
        init();
    }

    @SuppressLint( "SetTextI18n" )
    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_kirim = findViewById(R.id.btn_kirim);
        txt_userid = findViewById(R.id.user_id);
        l_kritik = findViewById(R.id.l_kritik);
        e_kritik = findViewById(R.id.e_kritik);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        txt_userid.setText("" + preferences.getInt("id_regis", 0));

        btn_kirim.setOnClickListener(v -> {
            if (validasi()) {
                setKirimData();
            }
        });
        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
        cekvalidasi();
    }

    public void getInputText() {
        user_id = txt_userid.getText().toString().trim();
        kritik = e_kritik.getText().toString().trim();
    }

    public void setKirimData() {
        getInputText();
        dataKritikModels = new ArrayList<>();
        dialog.setMessage("Loading...");
        dialog.show();
        kirimData = new StringRequest(Request.Method.POST, URLServer.POSTKRITIK, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    DataKritikModel kirimKritik = new DataKritikModel();
                    kirimKritik.setUser_id(data.getInt("user_id"));
                    kirimKritik.setKritik(data.getString("kritik"));
                    showDialog();
                } else {
                    showError(object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
                map.put("user_id", user_id);
                map.put("kritik", kritik);
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

    public void cekvalidasi() {
        e_kritik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (kritik.isEmpty()) {
                    l_kritik.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean validasi() {
        getInputText();
        if (kritik.isEmpty()) {
            l_kritik.setErrorEnabled(true);
            l_kritik.setError("Kolom kritik dan saran tidak boleh kosong!");
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setKirimData();
    }
}