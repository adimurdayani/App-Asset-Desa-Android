package com.jumarni.appassetdesa.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.jumarni.appassetdesa.model.DataUserRegis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditActivity extends AppCompatActivity {

    private ImageView btn_kembali;
    private Button btn_simpan;
    private TextInputLayout l_nama, l_username, l_email, l_nohp;
    private TextInputEditText e_nama, e_username, e_email, e_nohp;
    private String nama, email, username, nohp, id;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private StringRequest ubahProfil;
    private ProgressDialog dialog;
    private TextView id_regis;
    private ArrayList<DataUserRegis> dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    public void init() {
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_simpan = findViewById(R.id.btn_simpan);
        l_nama = findViewById(R.id.l_nama);
        l_email = findViewById(R.id.l_email);
        l_username = findViewById(R.id.l_username);
        l_nohp = findViewById(R.id.l_no_hp);
        e_nama = findViewById(R.id.e_nama);
        e_email = findViewById(R.id.e_email);
        e_username = findViewById(R.id.e_username);
        e_nohp = findViewById(R.id.e_no_hp);
        id_regis = findViewById(R.id.id_regis);

        e_nama.setText(preferences.getString("nama", ""));
        e_email.setText(preferences.getString("email", ""));
        e_username.setText(preferences.getString("username", ""));
        id_regis.setText("" + preferences.getInt("id_regis", 0));

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btn_simpan.setOnClickListener(v -> {
            if (validasi()) {
                setUbahProfil();
            }
        });
        cekvalidasi();
    }

    public void setUbahProfil() {
        dataUser = new ArrayList<>();
        dialog.setMessage("Loading...");
        dialog.show();
        ubahProfil = new StringRequest(Request.Method.POST, URLServer.POSTPROFILE, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    DataUserRegis postUser = new DataUserRegis();
                    postUser.setNama(data.getString("nama"));
                    postUser.setUsername(data.getString("username"));
                    postUser.setEmail(data.getString("email"));
                    postUser.setNo_hp(data.getString("no_hp"));

                    editor = preferences.edit();
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("username", data.getString("username"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("no_hp", data.getString("no_hp"));
                    editor.apply();

                    showDialog();
                    startActivity(new Intent(this, SettingActivity.class));
                    finish();
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
                map.put("id_regis", id);
                map.put("nama", nama);
                map.put("email", email);
                map.put("username", username);
                map.put("no_hp", nohp);
                return map;
            }
        };
        setKoneksiInternet();
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(ubahProfil);
    }

    private void setKoneksiInternet() {
        ubahProfil.setRetryPolicy(new RetryPolicy() {
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

    private void showDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sukses!")
                .setContentText("You clicked the button!")
                .show();
    }

    private void showError(String string) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(string)
                .show();
    }

    public void getinputtext() {
        nama = e_nama.getText().toString().trim();
        email = e_email.getText().toString().trim();
        username = e_username.getText().toString().trim();
        nohp = e_nohp.getText().toString().trim();
        id = id_regis.getText().toString().trim();
    }

    public void cekvalidasi() {
        getinputtext();

        e_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nama.isEmpty()) {
                    l_nama.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.isEmpty()) {
                    l_email.setErrorEnabled(false);
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    l_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        e_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.isEmpty()) {
                    l_username.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        e_nohp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nohp.isEmpty()) {
                    l_nohp.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validasi() {
        getinputtext();

        if (nama.isEmpty()) {
            l_nama.setErrorEnabled(true);
            l_nama.setError("Kolom nama tidak boleh kosong!");
            return false;
        }
        if (email.isEmpty()) {
            l_email.setErrorEnabled(true);
            l_email.setError("Kolom email tidak boleh kosong!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            l_email.setErrorEnabled(true);
            l_email.setError("Format email salah!. Contoh: gunakan @example.com");
            return false;
        }
        if (username.isEmpty()) {
            l_username.setErrorEnabled(true);
            l_username.setError("Kolom username tidak boleh kosong!");
            return false;
        }

        if (nohp.isEmpty()) {
            l_nohp.setErrorEnabled(true);
            l_nohp.setError("Kolom phone tidak boleh kosong!");
            return false;
        }
        return true;
    }
}